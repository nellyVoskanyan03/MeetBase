package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LecturerRespondRequest {

    @NotBlank(message = "Lecturer ID is required")
    private String lecturerId;

    @NotNull(message = "Accepted status is required")
    private Boolean accepted;

    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }

    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
}