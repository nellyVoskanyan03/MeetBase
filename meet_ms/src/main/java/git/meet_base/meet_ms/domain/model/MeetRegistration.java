package git.meet_base.meet_ms.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class MeetRegistration {

    private UUID id;
    private UUID meetId;
    private String studentId;
    private ZonedDateTime registeredAt;

    public MeetRegistration() {
    }

    public MeetRegistration(UUID id, UUID meetId, String studentId, ZonedDateTime registeredAt) {
        this.id = id;
        this.meetId = meetId;
        this.studentId = studentId;
        this.registeredAt = registeredAt;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMeetId() {
        return meetId;
    }

    public void setMeetId(UUID meetId) {
        this.meetId = meetId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ZonedDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(ZonedDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}