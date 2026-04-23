package git.meet_base.meet_ms.domain.service;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.model.MeetStatus;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class MeetService {

    private final MeetDomainRepository meetRepository;

    public MeetService(MeetDomainRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    public Meet initializeMeeting(Meet meet) {
        meet.setStatus(MeetStatus.CREATED);
        meet.setActualParticipants(0);

        return meetRepository.save(meet);
    }
}