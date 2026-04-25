package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.model.MeetStatus;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;
import git.meet_base.meet_ms.persistence.mapper.MeetMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MeetRepositoryAdapter implements MeetDomainRepository {

    private final MeetRepository meetRepository;

    public MeetRepositoryAdapter(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @Override
    public Meet save(Meet meetDomain) {
        MeetEntity entityToSave = MeetMapper.toEntity(meetDomain);

        MeetEntity savedEntity = meetRepository.save(entityToSave);


        return MeetMapper.toDomain(savedEntity);
    }
    @Override
    public Page<Meet> findByLecturerIdFiltered(String lecturerId, MeetStatus status, String companyId, Pageable pageable) {
        return meetRepository.findByLecturerIdDynamic(lecturerId, status, companyId, pageable)
                .map(MeetMapper::toDomain);
    }

    @Override
    public Page<Meet> findByIdInFiltered(List<UUID> meetIds, MeetStatus status, String companyId, Pageable pageable) {
        return meetRepository.findByIdInDynamic(meetIds, status, companyId, pageable)
                .map(MeetMapper::toDomain);
    }

    @Override
    public Page<Meet> findAllFiltered(MeetStatus status, String companyId, Pageable pageable) {
        return meetRepository.findAllDynamic(status, companyId, pageable)
                .map(MeetMapper::toDomain);
    }

    @Override
    public Optional<Meet> findById(UUID meetId) {
        return Optional.ofNullable(
                MeetMapper.toDomain(meetRepository.findById(meetId)));
    }
}