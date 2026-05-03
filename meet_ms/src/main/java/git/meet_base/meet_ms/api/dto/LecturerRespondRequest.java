package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.NotNull;

public class LecturerRespondRequest {

    @NotNull(message = "Accepted status is required")
    private Boolean accepted;

    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
}