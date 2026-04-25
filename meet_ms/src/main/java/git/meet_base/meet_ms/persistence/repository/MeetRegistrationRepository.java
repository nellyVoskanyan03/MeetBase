package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.persistence.entity.MeetRegistrationEntity;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.UUID;

public interface MeetRegistrationRepository extends Repository<MeetRegistrationEntity, UUID> {

    MeetRegistrationEntity save(MeetRegistrationEntity entity);

    List<MeetRegistrationEntity> findByStudentId(String studentId);

    List<MeetRegistrationEntity> findByMeetId(UUID meetId);

    boolean existsByMeetIdAndStudentId(UUID meetId, String studentId);
}