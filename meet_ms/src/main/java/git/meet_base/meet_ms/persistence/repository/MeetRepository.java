package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface MeetRepository extends Repository<MeetEntity, UUID> {
    MeetEntity save(MeetEntity entity);

    List<MeetEntity> findByIdIn(List<UUID> ids);

    List<MeetEntity> findByLecturerId(String id);

    List<MeetEntity> findAll();
}