package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.domain.repository.MeetDomainRepository;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;
import git.meet_base.meet_ms.persistence.mapper.MeetMapper;
import org.springframework.stereotype.Component;

@Component

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
}