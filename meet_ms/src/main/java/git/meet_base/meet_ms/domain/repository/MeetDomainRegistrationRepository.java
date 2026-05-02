package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.MeetRegistration;

import java.util.List;
import java.util.UUID;

public interface MeetDomainRegistrationRepository {
    List<MeetRegistration> findByStudentId(UUID studentId);

    MeetRegistration save(MeetRegistration registration);

    boolean existsByMeetIdAndStudentId(UUID meetId, UUID studentId);

    List<MeetRegistration> findByMeetId(UUID id);
}
