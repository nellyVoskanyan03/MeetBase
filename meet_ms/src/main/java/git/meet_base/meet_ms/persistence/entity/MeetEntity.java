package git.meet_base.meet_ms.persistence.entity;

import git.meet_base.meet_ms.domain.model.MeetStatus;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "meets")
public class MeetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String course;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "lecturer_id")
    private UUID lecturerId;

    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @Column(nullable = false)
    private String place;

    @Column(name = "min_student_count", nullable = false)
    private int minStudentCount;

    @Column(name = "actual_participants", nullable = false)
    private int actualParticipants = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetStatus status;

    @Column(name = "google_calendar_event_id")
    private String googleCalendarEventId;

    @Column(name = "hangout_link")
    private String hangoutLink;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
        if (this.status == null) {
            this.status = MeetStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
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