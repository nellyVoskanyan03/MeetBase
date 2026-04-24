package git.meet_base.meet_ms.domain.service;

import git.meet_base.meet_ms.domain.exception.ResourceNotFoundException;
import git.meet_base.meet_ms.domain.exception.UnauthorizedActionException;
import git.meet_base.meet_ms.domain.model.*;
import git.meet_base.meet_ms.domain.repository.MeetDomainRegistrationRepository;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MeetService {

    private final MeetDomainRepository meetDomainRepository;
    private final MeetDomainRegistrationRepository meetDomainRegistrationRepository;

    public MeetService(MeetDomainRepository meetDomainRepository, MeetDomainRegistrationRepository meetDomainRegistrationRepository) {
        this.meetDomainRepository = meetDomainRepository;
        this.meetDomainRegistrationRepository = meetDomainRegistrationRepository;
    }

    public Meet initializeMeeting(Meet meet) {
        meet.setStatus(MeetStatus.CREATED);
        meet.setActualParticipants(0);
        // TODO: Publish "MeetCreatedEvent" to Kafka and Notify the assigned Lecturer

        return meetDomainRepository.save(meet);
    }

    public List<Meet> getFilteredMeets(
            UserRole role,
            MeetStatus status,
            String companyId,
            String userId
    ) {
        List<Meet> userMeets = new ArrayList<>();
        if (role != null && userId != null) {
            //Todo for later: role and id won't be needed when the JWT is added
            switch (role) {
                case STUDENT -> {
                    List<MeetRegistration> registrations = meetDomainRegistrationRepository.findByStudentId(userId);
                    List<UUID> meetIds = registrations.stream()
                            .map(MeetRegistration::getMeetId)
                            .toList();

                    if (!meetIds.isEmpty()) {
                        userMeets = meetDomainRepository.findByIdIn(meetIds);
                    }
                }
                case LECTURER -> userMeets = meetDomainRepository.findByLecturerId(userId);
                //Todo for later: use the company id of the manager to get the meets
                case MANAGER -> userMeets = meetDomainRepository.findAll();
            }

        }
        return userMeets.stream()
                .filter(meet -> status == null || meet.getStatus() == status)
                .filter(meet -> companyId == null || companyId.equals(meet.getCompanyId()))
                .toList();
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

        if (accepted) {
            meet.setStatus(MeetStatus.PENDING);
        } else {
            meet.setStatus(MeetStatus.CANCELLED);
        }

        // TODO: Publish "LecturerRespondedEvent" to Kafka and Notify the Company

        return meetDomainRepository.save(meet);
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

        if (meet.getActualParticipants() >= meet.getMinStudentCount()) {
            // TODO for later: add notification for managers to approve
        }

        return meetDomainRepository.save(meet);
    }

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

        //TODO: add GoogleCalendarClient call
        String generatedEventId = "";
        String generatedLink = "https://meet.google.com/";

        meet.setGoogleCalendarEventId(generatedEventId);
        meet.setHangoutLink(generatedLink);

        // TODO: Publish final "Confirmed" event to Kafka and Notify the Company, Lecturer, and all Registered Students

        return meetDomainRepository.save(meet);
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

            // TODO: Call Google Calendar API to reschedule the event

            // TODO: Publish event to Kafka and Notify all participants (Students, Lecturer, Company) about the time/location change.
        }

        return meetDomainRepository.save(meet);
    }

    public Meet cancelMeeting(UUID meetId) {
        Meet meet = meetDomainRepository.findById(meetId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + meetId));

        MeetStatus previousStatus = meet.getStatus();

        meet.setStatus(MeetStatus.CANCELLED);

        if (previousStatus == MeetStatus.APPROVED) {

            // TODO: Call Google Calendar API to delete the event from everyone's calendars

            // TODO: Publish "MeetingCancelledEvent" to Kafka Alert the Lecturer, Company, and Students that the event is off.
        } else if (previousStatus == MeetStatus.PENDING) {
            // TODO: Publish "MeetingCancelledEvent" to Kafka Alert the Lecturer, Company, and Students that the event is off.
        }

        return meetDomainRepository.save(meet);
    }
}