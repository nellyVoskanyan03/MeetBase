package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.MeetRegistration;

import java.util.List;

public interface MeetDomainRegistrationRepository {
    List<MeetRegistration> findByStudentId(String studentId);
}
