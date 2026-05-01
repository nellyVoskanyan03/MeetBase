package git.meet_base.notification_ms.infrastructure.messaging.dto;

import java.util.List;
import java.util.UUID;

public class MeetEventPayload {

    private UUID meetId;
    private String targetRole;
    private UUID companyId;
    private List<UUID> targetUserIds;
    private String action;
    private String message;

    public MeetEventPayload() {}

    public UUID getMeetId() { return meetId; }
    public void setMeetId(UUID meetId) { this.meetId = meetId; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }

    public List<UUID> getTargetUserIds() { return targetUserIds; }
    public void setTargetUserEmails(List<UUID> targetUserIds) { this.targetUserIds = targetUserIds; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}