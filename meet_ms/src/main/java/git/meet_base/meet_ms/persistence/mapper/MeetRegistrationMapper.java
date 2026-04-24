package git.meet_base.meet_ms.persistence.mapper;

import git.meet_base.meet_ms.domain.model.MeetRegistration;
import git.meet_base.meet_ms.persistence.entity.MeetRegistrationEntity;

public class MeetRegistrationMapper {

    public static MeetRegistration toDomain(MeetRegistrationEntity entity) {
        if (entity == null) {
            return null;
        }

        MeetRegistration domain = new MeetRegistration();
        domain.setId(entity.getId());
        domain.setMeetId(entity.getMeetId());
        domain.setStudentId(entity.getStudentId());
        domain.setRegisteredAt(entity.getRegisteredAt());

        return domain;
    }

    public static MeetRegistrationEntity toEntity(MeetRegistration domain) {
        if (domain == null) {
            return null;
        }

        MeetRegistrationEntity entity = new MeetRegistrationEntity();
        entity.setId(domain.getId());
        entity.setMeetId(domain.getMeetId());
        entity.setStudentId(domain.getStudentId());
        entity.setRegisteredAt(domain.getRegisteredAt());

        return entity;
    }
}