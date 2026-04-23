package git.meet_base.meet_ms.persistence.mapper;

import git.meet_base.meet_ms.domain.model.Meet;
import git.meet_base.meet_ms.persistence.entity.MeetEntity;

public class MeetMapper {
    public static Meet toDomain(MeetEntity entity) {
        if (entity == null) {
            return null;
        }

        Meet meet = new Meet();
        meet.setId(entity.getId());
        meet.setCourse(entity.getCourse());
        meet.setCompanyId(entity.getCompanyId());
        meet.setLecturerId(entity.getLecturerId());
        meet.setDateTime(entity.getDateTime());
        meet.setPlace(entity.getPlace());
        meet.setMinStudentCount(entity.getMinStudentCount());
        meet.setActualParticipants(entity.getActualParticipants());
        meet.setStatus(entity.getStatus());
        meet.setGoogleCalendarEventId(entity.getGoogleCalendarEventId());
        meet.setHangoutLink(entity.getHangoutLink());
        meet.setCreatedAt(entity.getCreatedAt());
        meet.setUpdatedAt(entity.getUpdatedAt());

        return meet;
    }

    public static MeetEntity toEntity(Meet domain) {
        if (domain == null) {
            return null;
        }

        MeetEntity entity = new MeetEntity();
        entity.setId(domain.getId());
        entity.setCourse(domain.getCourse());
        entity.setCompanyId(domain.getCompanyId());
        entity.setLecturerId(domain.getLecturerId());
        entity.setDateTime(domain.getDateTime());
        entity.setPlace(domain.getPlace());
        entity.setMinStudentCount(domain.getMinStudentCount());
        entity.setActualParticipants(domain.getActualParticipants());
        entity.setStatus(domain.getStatus());
        entity.setGoogleCalendarEventId(domain.getGoogleCalendarEventId());
        entity.setHangoutLink(domain.getHangoutLink());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}