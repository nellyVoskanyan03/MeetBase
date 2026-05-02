package git.meet_base.auth_ms.repository;


import git.meet_base.auth_ms.model.User;
import git.meet_base.auth_ms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    interface UserEmailProjection {
        UUID getId();
        String getEmail();
    }

    @Query("SELECT u.id as id, u.email as email FROM User u WHERE u.id IN :userIds")
    List<UserEmailProjection> findEmailsByUserIds(@Param("userIds") List<UUID> userIds);

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:companyId IS NULL OR u.companyId = :companyId) AND " +
            "(:isActive IS NULL OR u.isActive = :isActive)")
    List<User> findUsersWithFilters(
            @Param("role") UserRole role,
            @Param("companyId") UUID companyId,
            @Param("isActive") Boolean isActive
    );
}