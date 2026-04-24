package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;
import git.meet_base.meet_ms.persistence.mapper.MeetMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<Meet> findByIdIn(List<UUID> ids) {
        List<MeetEntity> entities = meetRepository.findByIdIn(ids);

        return entities.stream()
                .map(MeetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meet> findByLecturerId(String id) {
        List<MeetEntity> entities = meetRepository.findByLecturerId(id);

        return entities.stream()
                .map(MeetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meet> findAll() {
        List<MeetEntity> entities = meetRepository.findAll();

        return entities.stream()
                .map(MeetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Meet> findById(UUID meetId) {
        return Optional.ofNullable(
                MeetMapper.toDomain(meetRepository.findById(meetId)));
    }
}