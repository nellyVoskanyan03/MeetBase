package git.meet_base.meet_ms.api.dto;

import java.util.UUID;

public class LecturerRespondResponse {

    private String message;
    private UUID meetId;
    private String status;

    public LecturerRespondResponse(String message, UUID meetId, String status) {
        this.message = message;
        this.meetId = meetId;
        this.status = status;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UUID getMeetId() { return meetId; }
    public void setMeetId(UUID meetId) { this.meetId = meetId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}