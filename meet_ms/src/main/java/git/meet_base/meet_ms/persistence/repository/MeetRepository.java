package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.MeetStatus;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MeetRepository extends Repository<MeetEntity, UUID> {
    MeetEntity save(MeetEntity entity);

    MeetEntity findById(UUID meetId);

    @Query("SELECT m FROM MeetEntity m WHERE " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:companyId IS NULL OR m.companyId = :companyId)")
    Page<MeetEntity> findAllDynamic(
            @Param("status") MeetStatus status,
            @Param("companyId") UUID companyId,
            Pageable pageable);

    @Query("""
        SELECT m FROM MeetEntity m 
        WHERE m.companyId = :studentCompanyId
        AND (
            m.status = git.meet_base.meet_ms.domain.model.MeetStatus.PENDING
            OR (m.status = git.meet_base.meet_ms.domain.model.MeetStatus.APPROVED AND m.id IN :registeredIds)
        )
        AND (:filterStatus IS NULL OR m.status = :filterStatus)
    """)
    Page<MeetEntity> findForStudent(
            @Param("registeredIds") List<UUID> registeredIds,
            @Param("studentCompanyId") UUID studentCompanyId,
            @Param("filterStatus") MeetStatus filterStatus,
            Pageable pageable
    );

    @Query("""
                SELECT m FROM MeetEntity m 
                WHERE m.companyId = :companyId 
                AND (:filterStatus IS NULL OR m.status = :filterStatus)
            """)
    Page<MeetEntity> findByManagerIdFiltered(
            @Param("managerId") UUID managerId,
            @Param("filterStatus") MeetStatus filterStatus,
            @Param("companyId") UUID companyId,
            Pageable pageable
    );

    @Query("SELECT m FROM MeetEntity m WHERE m.lecturerId = :lecturerId AND " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:companyId IS NULL OR m.companyId = :companyId)")
    Page<MeetEntity> findByLecturerIdDynamic(
            @Param("lecturerId") UUID lecturerId,
            @Param("status") MeetStatus status,
            @Param("companyId") UUID companyId,
            Pageable pageable);

    @Query("SELECT m FROM MeetEntity m WHERE m.id IN :meetIds AND " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:companyId IS NULL OR m.companyId = :companyId)")
    Page<MeetEntity> findByIdInDynamic(
            @Param("meetIds") List<UUID> meetIds,
            @Param("status") MeetStatus status,
            @Param("companyId") UUID companyId,
            Pageable pageable);
}