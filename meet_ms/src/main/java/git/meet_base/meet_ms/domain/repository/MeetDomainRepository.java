package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.Meet;

public interface MeetDomainRepository {
    Meet save(Meet meet);
}
