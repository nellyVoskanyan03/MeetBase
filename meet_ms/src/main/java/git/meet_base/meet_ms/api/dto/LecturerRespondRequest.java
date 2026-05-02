package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class LecturerRespondRequest {

    @NotNull(message = "Lecturer ID is required")
    private UUID lecturerId;

    @NotNull(message = "Accepted status is required")
    private Boolean accepted;

    public UUID getLecturerId() { return lecturerId; }
    public void setLecturerId(UUID lecturerId) { this.lecturerId = lecturerId; }

    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
}