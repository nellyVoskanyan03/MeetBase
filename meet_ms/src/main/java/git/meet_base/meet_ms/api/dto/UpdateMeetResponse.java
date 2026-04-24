package git.meet_base.meet_ms.api.dto;

import java.util.Map;
import java.util.UUID;

public class UpdateMeetResponse {

    private final String message;
    private final UUID meetId;
    private final Map<String, Object> updatedFields;
    private final String status;

    public UpdateMeetResponse(String message, UUID meetId, Map<String, Object> updatedFields, String status) {
        this.message = message;
        this.meetId = meetId;
        this.updatedFields = updatedFields;
        this.status = status;
    }

    public String getMessage() { return message; }
    public UUID getMeetId() { return meetId; }
    public Map<String, Object> getUpdatedFields() { return updatedFields; }
    public String getStatus() { return status; }
}