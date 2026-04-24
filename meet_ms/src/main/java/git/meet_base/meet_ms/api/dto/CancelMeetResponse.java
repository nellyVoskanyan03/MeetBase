package git.meet_base.meet_ms.api.dto;

import java.util.UUID;

public class CancelMeetResponse {

    private final String message;
    private  final UUID meetId;
    private  final String status;

    public CancelMeetResponse(String message, UUID meetId, String status) {
        this.message = message;
        this.meetId = meetId;
        this.status = status;
    }

    public String getMessage() { return message; }
    public UUID getMeetId() { return meetId; }
    public String getStatus() { return status; }
}