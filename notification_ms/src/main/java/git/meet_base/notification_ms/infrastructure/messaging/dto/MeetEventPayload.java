package git.meet_base.notification_ms.infrastructure.messaging.dto;

import java.util.List;

public class MeetEventPayload {

    private String meetId;
    private String targetRole;
    private String companyId;
    private List<String> targetUserIds;
    private String action;
    private String message;

    public MeetEventPayload() {}

    public String getMeetId() { return meetId; }
    public void setMeetId(String meetId) { this.meetId = meetId; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public List<String> getTargetUserIds() { return targetUserIds; }
    public void setTargetUserEmails(List<String> targetUserIds) { this.targetUserIds = targetUserIds; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}