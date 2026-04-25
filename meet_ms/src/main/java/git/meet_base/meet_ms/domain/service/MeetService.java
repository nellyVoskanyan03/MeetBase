package git.meet_base.meet_ms.domain.service;

import git.meet_base.meet_ms.api.event.MeetActionType;
import git.meet_base.meet_ms.api.event.MeetEventProducer;
import git.meet_base.meet_ms.api.event.MeetNotificationEvent;
import git.meet_base.meet_ms.domain.exception.ResourceNotFoundException;
import git.meet_base.meet_ms.domain.exception.UnauthorizedActionException;
import git.meet_base.meet_ms.domain.model.*;
import git.meet_base.meet_ms.domain.repository.MeetDomainRegistrationRepository;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import git.meet_base.meet_ms.infrastructure.CalendarEventResult;
import git.meet_base.meet_ms.infrastructure.GoogleCalendarClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MeetService {

    private final MeetDomainRepository meetDomainRepository;
    private final MeetDomainRegistrationRepository meetDomainRegistrationRepository;
    private final MeetEventProducer meetEventProducer;
    private final GoogleCalendarClient googleCalendarClient;

    public MeetService(MeetDomainRepository meetDomainRepository, MeetDomainRegistrationRepository meetDomainRegistrationRepository, MeetEventProducer meetEventProducer, GoogleCalendarClient googleCalendarClient) {
        this.meetDomainRepository = meetDomainRepository;
        this.meetDomainRegistrationRepository = meetDomainRegistrationRepository;
        this.meetEventProducer = meetEventProducer;
        this.googleCalendarClient = googleCalendarClient;
    }

    public Meet initializeMeeting(Meet meet) {
        meet.setStatus(MeetStatus.CREATED);
        meet.setActualParticipants(0);

        Meet savedMeet = meetDomainRepository.save(meet);

        MeetNotificationEvent event = new MeetNotificationEvent(
                savedMeet.getId(),
                UserRole.LECTURER,
                List.of(savedMeet.getLecturerId()),
                MeetActionType.MEET_CREATED,
                "A new meeting requires your approval."
        );
        meetEventProducer.sendNotification(event);

        return savedMeet;
    }

    public Page<Meet> getFilteredMeets(
            UserRole role,
            MeetStatus status,
            String companyId,
            String userId,
            Pageable pageable
    ) {
        if (role != null && userId != null) {
            //Todo for later: role and id won't be needed when the JWT is added
            switch (role) {
                case STUDENT -> {
                    List<MeetRegistration> registrations = meetDomainRegistrationRepository.findByStudentId(userId);
                    List<UUID> meetIds = registrations.stream()
                            .map(MeetRegistration::getMeetId)
                            .toList();

                    if (!meetIds.isEmpty()) {
                        return meetDomainRepository.findByIdInFiltered(meetIds, status, companyId, pageable);
                    }
                }
                case LECTURER -> {
                    return meetDomainRepository.findByLecturerIdFiltered(userId, status, companyId, pageable);
                }
                case MANAGER -> {
                    return meetDomainRepository.findAllFiltered(status, companyId, pageable);
                }
            }
        }
        return Page.empty(pageable);
    }

    public Meet respondToInvitation(UUID meetId, String lecturerId, Boolean accepted) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        if (!lecturerId.equals(meet.getLecturerId())) {
            throw new UnauthorizedActionException("Unauthorized: Lecturer ID does not match the meeting record.");
        }

        if (meet.getStatus() != MeetStatus.CREATED) {
            throw new UnauthorizedActionException("Only meetings with status created can be responded to.");
        }

        MeetActionType actionType;
        String message;

        if (accepted) {
            meet.setStatus(MeetStatus.PENDING);
            actionType = MeetActionType.INVITATION_ACCEPTED;
            message = "The assigned lecturer has accepted the meeting invitation.";
        } else {
            meet.setStatus(MeetStatus.CANCELLED);
            actionType = MeetActionType.INVITATION_REJECTED;
            message = "The assigned lecturer has rejected the meeting invitation.";
        }

        Meet savedMeet = meetDomainRepository.save(meet);
        MeetNotificationEvent event = new MeetNotificationEvent(
                savedMeet.getId(),
                UserRole.COMPANY,
                List.of(savedMeet.getCompanyId()),
                actionType,
                message
        );
        meetEventProducer.sendNotification(event);

        return savedMeet;
    }

    public Meet registerStudent(UUID meetId, String studentId) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        if (meet.getStatus() != MeetStatus.PENDING) {
            throw new UnauthorizedActionException("This meeting is not currently open for registration.");
        }

        if (meetDomainRegistrationRepository.existsByMeetIdAndStudentId(meetId, studentId)) {
            throw new UnauthorizedActionException("Student " + studentId + " is already registered for this meeting.");
        }

        MeetRegistration registration = new MeetRegistration();
        registration.setMeetId(meetId);
        registration.setStudentId(studentId);
        registration.setRegisteredAt(ZonedDateTime.now());

        meetDomainRegistrationRepository.save(registration);

        meet.setActualParticipants(meet.getActualParticipants() + 1);

        if (meet.getActualParticipants() == meet.getMinStudentCount()) {
            MeetNotificationEvent event = new MeetNotificationEvent(
                    meet.getId(),
                    UserRole.MANAGER,
                    null, // Null means broadcast to ALL Managers
                    MeetActionType.APPROVAL_REQUIRED,
                    "Meeting has reached the minimum student threshold and is ready for approval."
            );
            meetEventProducer.sendNotification(event);
        }

        return meetDomainRepository.save(meet);
    }

    @Transactional
    public Meet approveMeeting(UUID meetId) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        if (meet.getStatus() != MeetStatus.PENDING) {
            throw new UnauthorizedActionException("Only pending meetings can be approved.");
        }

        if (meet.getActualParticipants() < meet.getMinStudentCount()) {
            throw new UnauthorizedActionException(
                    "Cannot approve: Minimum student count (" + meet.getMinStudentCount() + ") not met yet."
            );
        }

        meet.setStatus(MeetStatus.APPROVED);


        try {
            CalendarEventResult googleResult = googleCalendarClient.createMeetingWithHangoutLink(
                    "University Class: " + meet.getCourse(),
                    "Place: " + meet.getPlace(),
                    meet.getDateTime().toString(),
                    meet.getDateTime().plusHours(1).toString()
            );

            meet.setGoogleCalendarEventId(googleResult.getEventId());
            meet.setHangoutLink(googleResult.getMeetLink());

        } catch (Exception e) {
            throw new RuntimeException("Google Calendar integration failed. Meeting not approved.", e);
        }

        Meet savedMeet = meetDomainRepository.save(meet);

        notifyAllParticipants(
                savedMeet,
                MeetActionType.MEET_APPROVED,
                "The meeting has been officially approved. Calendar links are generated!"
        );

        return savedMeet;
    }

    public Meet updateMeeting(UUID meetId, UpdateMeetCommand command) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        boolean isUpdated = false;

        if (command.getPlace() != null && !command.getPlace().isBlank()) {
            meet.setPlace(command.getPlace());
            isUpdated = true;
        }

        if (command.getDateTime() != null) {
            meet.setDateTime(command.getDateTime());
            isUpdated = true;
        }

        if (!isUpdated) {
            return meet;
        }

        if (meet.getStatus() == MeetStatus.APPROVED) {

            try {
                googleCalendarClient.updateMeetingEvent(
                        meet.getGoogleCalendarEventId(),
                        "University Class: " + meet.getCourse(),
                        "Location updated to: " + meet.getPlace(),
                        meet.getDateTime().toString(),
                        meet.getDateTime().plusHours(1).toString()
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync meeting update with Google Calendar: " + e.getMessage(), e);
            }

            notifyAllParticipants(
                    meet,
                    MeetActionType.MEET_UPDATED,
                    "The time or location for your meeting has changed."
            );
        }

        return meetDomainRepository.save(meet);
    }

    public Meet cancelMeeting(UUID meetId) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        MeetStatus previousStatus = meet.getStatus();

        meet.setStatus(MeetStatus.CANCELLED);

        if (previousStatus == MeetStatus.APPROVED) {

            if (meet.getGoogleCalendarEventId() != null) {
                try {
                    googleCalendarClient.deleteMeetingEvent(meet.getGoogleCalendarEventId());
                } catch (Exception e) {
                    System.err.println("Note: Could not delete Google Event: " + e.getMessage());
                }
            }

            notifyAllParticipants(
                    meet,
                    MeetActionType.MEET_CANCELLED,
                    "The meeting has been cancelled."
            );
        } else if (previousStatus == MeetStatus.PENDING) {
            notifyAllParticipants(
                    meet,
                    MeetActionType.MEET_CANCELLED,
                    "The pending meeting has been cancelled."
            );
        }

        return meetDomainRepository.save(meet);
    }

    private void notifyAllParticipants(Meet meet, MeetActionType action, String message) {

        meetEventProducer.sendNotification(new MeetNotificationEvent(
                meet.getId(), UserRole.COMPANY, List.of(meet.getCompanyId()), action, message
        ));

        if (meet.getLecturerId() != null) {
            meetEventProducer.sendNotification(new MeetNotificationEvent(
                    meet.getId(), UserRole.LECTURER, List.of(meet.getLecturerId()), action, message
            ));
        }

        List<String> registeredStudentIds = meetDomainRegistrationRepository.findByMeetId(meet.getId())
                .stream()
                .map(MeetRegistration::getStudentId)
                .toList();

        if (!registeredStudentIds.isEmpty()) {
            meetEventProducer.sendNotification(new MeetNotificationEvent(
                    meet.getId(), UserRole.STUDENT, registeredStudentIds, action, message
            ));
        }
    }

}