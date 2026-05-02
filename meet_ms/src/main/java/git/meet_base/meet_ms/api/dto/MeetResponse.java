package git.meet_base.meet_ms.api.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public class MeetResponse {

    private UUID meetId;
    private String course;
    private UUID companyId;
    private UUID lecturerId;
    private ZonedDateTime dateTime;
    private String place;
    private int minStudentCount;
    private int actualParticipants;
    private String status;
    private String googleCalendarEventId;
    private String hangoutLink;

    public MeetResponse() {

    }


    public UUID getMeetId() { return meetId; }
    public void setMeetId(UUID meetId) { this.meetId = meetId; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }

    public UUID getLecturerId() { return lecturerId; }
    public void setLecturerId(UUID lecturerId) { this.lecturerId = lecturerId; }

    public ZonedDateTime getDateTime() { return dateTime; }
    public void setDateTime(ZonedDateTime dateTime) { this.dateTime = dateTime; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public int getMinStudentCount() { return minStudentCount; }
    public void setMinStudentCount(int minStudentCount) { this.minStudentCount = minStudentCount; }

    public int getActualParticipants() { return actualParticipants; }
    public void setActualParticipants(int actualParticipants) { this.actualParticipants = actualParticipants; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGoogleCalendarEventId() { return googleCalendarEventId; }
    public void setGoogleCalendarEventId(String googleCalendarEventId) { this.googleCalendarEventId = googleCalendarEventId; }

    public String getHangoutLink() { return hangoutLink; }
    public void setHangoutLink(String hangoutLink) { this.hangoutLink = hangoutLink; }
}