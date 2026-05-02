package git.meet_base.meet_ms.domain.repository;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.model.MeetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetDomainRepository {
    Meet save(Meet meet);

    Page<Meet> findByIdInFiltered(List<UUID> meetIds, MeetStatus status, UUID companyId, Pageable pageable);

    Page<Meet> findByLecturerIdFiltered(UUID lecturerId, MeetStatus status, UUID companyId, Pageable pageable);

    Page<Meet> findAllFiltered(MeetStatus status, UUID companyId, Pageable pageable);

    Optional<Meet> findById(UUID meetId);
}
