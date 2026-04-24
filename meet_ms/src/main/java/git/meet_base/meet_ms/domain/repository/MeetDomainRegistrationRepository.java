package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.MeetRegistration;

import java.util.List;
import java.util.UUID;

public interface MeetDomainRegistrationRepository {
    List<MeetRegistration> findByStudentId(String studentId);

    MeetRegistration save(MeetRegistration registration);

    boolean existsByMeetIdAndStudentId(UUID meetId, String studentId);
}
