package git.meet_base.meet_ms.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Meet {
    private UUID id;
    private String course;
    private UUID companyId;
    private UUID lecturerId;
    private ZonedDateTime dateTime;
    private String place;
    private int minStudentCount;
    private int actualParticipants;
    private MeetStatus status;
    private String googleCalendarEventId;
    private String hangoutLink;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Meet() {
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public MeetStatus getStatus() { return status; }
    public void setStatus(MeetStatus status) { this.status = status; }

    public String getGoogleCalendarEventId() { return googleCalendarEventId; }
    public void setGoogleCalendarEventId(String googleCalendarEventId) { this.googleCalendarEventId = googleCalendarEventId; }

    public String getHangoutLink() { return hangoutLink; }
    public void setHangoutLink(String hangoutLink) { this.hangoutLink = hangoutLink; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
