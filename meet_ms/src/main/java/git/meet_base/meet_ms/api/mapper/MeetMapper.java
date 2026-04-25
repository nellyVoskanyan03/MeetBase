package git.meet_base.meet_ms.api.mapper;

import git.meet_base.meet_ms.api.dto.CreateMeetRequest;
import git.meet_base.meet_ms.api.dto.MeetResponse;
import git.meet_base.meet_ms.domain.model.Meet;

public class MeetMapper {

    public static Meet toDomain(CreateMeetRequest dto) {
        if (dto == null) {
            return null;
        }

        Meet meet = new Meet();
        meet.setCourse(dto.getCourse());
        meet.setCompanyId(dto.getCompanyId());
        meet.setLecturerId(dto.getLecturerId());
        meet.setDateTime(dto.getDateTime());
        meet.setPlace(dto.getPlace());
        meet.setMinStudentCount(dto.getMinStudentCount());

        return meet;
    }

    public static MeetResponse toDto(Meet domain) {
        if (domain == null) {
            return null;
        }

        MeetResponse dto = new MeetResponse();
        dto.setMeetId(domain.getId());
        dto.setCourse(domain.getCourse());
        dto.setCompanyId(domain.getCompanyId());
        dto.setLecturerId(domain.getLecturerId());
        dto.setDateTime(domain.getDateTime());
        dto.setPlace(domain.getPlace());
        dto.setMinStudentCount(domain.getMinStudentCount());
        dto.setActualParticipants(domain.getActualParticipants());
        dto.setStatus(domain.getStatus().name());
        dto.setHangoutLink(domain.getHangoutLink());
        dto.setGoogleCalendarEventId(domain.getGoogleCalendarEventId());

        return dto;
    }
}