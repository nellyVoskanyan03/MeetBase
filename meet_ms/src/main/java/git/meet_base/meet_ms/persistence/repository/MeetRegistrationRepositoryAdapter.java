package git.meet_base.meet_ms.persistence.repository;

import git.meet_base.meet_ms.domain.model.MeetRegistration;
import git.meet_base.meet_ms.domain.repository.MeetDomainRegistrationRepository;
import git.meet_base.meet_ms.persistence.entity.MeetRegistrationEntity;
import git.meet_base.meet_ms.persistence.mapper.MeetRegistrationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MeetRegistrationRepositoryAdapter implements MeetDomainRegistrationRepository {


    private final MeetRegistrationRepository meetRegistrationRepository;

    public MeetRegistrationRepositoryAdapter(MeetRegistrationRepository meetRegistrationRepository) {
        this.meetRegistrationRepository = meetRegistrationRepository;
    }

    @Override
    public List<MeetRegistration> findByStudentId(String studentId) {
        List<MeetRegistrationEntity> entities = meetRegistrationRepository.findByStudentId(studentId);
        return entities
                .stream()
                .map(MeetRegistrationMapper::toDomain)
                .toList();
    }

    @Override
    public MeetRegistration save(MeetRegistration registration) {
        MeetRegistrationEntity entityToSave = MeetRegistrationMapper.toEntity(registration);

        MeetRegistrationEntity savedEntity = meetRegistrationRepository.save(entityToSave);


        return MeetRegistrationMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByMeetIdAndStudentId(UUID meetId, String studentId) {
        return meetRegistrationRepository.existsByMeetIdAndStudentId(meetId, studentId);
    }
}