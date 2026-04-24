package git.meet_base.meet_ms.persistence.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "meet_registrations")
public class MeetRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "meet_id", nullable = false)
    private UUID meetId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private ZonedDateTime registeredAt;

    @PrePersist
    protected void onCreate() {
        this.registeredAt = ZonedDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMeetId() { return meetId; }
    public void setMeetId(UUID meetId) { this.meetId = meetId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public ZonedDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(ZonedDateTime registeredAt) { this.registeredAt = registeredAt; }
}