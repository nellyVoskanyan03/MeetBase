package git.meet_base.meet_ms.api.dto;

import java.util.UUID;

public class StudentRegisterResponse {

    private final String message;
    private final UUID meetId;
    private final int actualParticipants;
    private final String status;

    public StudentRegisterResponse(String message, UUID meetId, int actualParticipants, String status) {
        this.message = message;
        this.meetId = meetId;
        this.actualParticipants = actualParticipants;
        this.status = status;
    }

    public String getMessage() { return message; }
    public UUID getMeetId() { return meetId; }
    public int getActualParticipants() { return actualParticipants; }
    public String getStatus() { return status; }
}