package git.meet_base.meet_ms.api.dto;

import java.util.UUID;

public class ApproveMeetResponse {

    private final String message;
    private final UUID meetId;
    private final String status;
    private final String googleCalendarEventId;
    private final String hangoutLink;

    public ApproveMeetResponse(String message, UUID meetId, String status, String googleCalendarEventId, String hangoutLink) {
        this.message = message;
        this.meetId = meetId;
        this.status = status;
        this.googleCalendarEventId = googleCalendarEventId;
        this.hangoutLink = hangoutLink;
    }

    public String getMessage() { return message; }
    public UUID getMeetId() { return meetId; }
    public String getStatus() { return status; }
    public String getGoogleCalendarEventId() { return googleCalendarEventId; }
    public String getHangoutLink() { return hangoutLink; }
}