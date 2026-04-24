package git.meet_base.meet_ms.domain.service;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.model.MeetRegistration;
import git.meet_base.meet_ms.domain.model.MeetStatus;
import git.meet_base.meet_ms.domain.model.UserRole;
import git.meet_base.meet_ms.domain.repository.MeetDomainRegistrationRepository;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import git.meet_base.meet_ms.persistence.repository.MeetRegistrationRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

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
                case LECTURER -> {
                    userMeets = meetDomainRepository.findByLecturerId(userId);
                }
                case MANAGER -> {
                    userMeets = meetDomainRepository.findAll();
                }
            }

        }
        return userMeets.stream()
                .filter(meet -> status == null || meet.getStatus() == status)
                .filter(meet -> companyId == null || companyId.equals(meet.getCompanyId()))
                .toList();
    }
}