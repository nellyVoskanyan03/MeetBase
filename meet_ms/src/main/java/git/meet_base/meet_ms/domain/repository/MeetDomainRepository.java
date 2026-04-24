package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.Meet;

import java.util.List;
import java.util.UUID;

public interface MeetDomainRepository {
    Meet save(Meet meet);

    List<Meet> findByIdIn(List<UUID> ids);

    List<Meet> findByLecturerId(String userId);

    List<Meet> findAll();
}
