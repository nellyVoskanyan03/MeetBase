package git.meet_base.meet_ms.api.event;

import git.meet_base.meet_ms.domain.model.UserRole;

import java.util.List;
import java.util.UUID;

public class MeetNotificationEvent {
    private final UUID meetId;
    private final UserRole targetRole;
    private final List<String> targetUserIds;
    private final MeetActionType action;
    private final String message;

    public MeetNotificationEvent(UUID meetId, UserRole targetRole, List<String> targetUserIds, MeetActionType action, String message) {
        this.meetId = meetId;
        this.targetRole = targetRole;
        this.targetUserIds = targetUserIds;
        this.action = action;
        this.message = message;
    }

    public UUID getMeetId() { return meetId; }
    public UserRole getTargetRole() { return targetRole; }
    public List<String> getTargetUserIds() { return targetUserIds; }
    public MeetActionType getAction() { return action; }
    public String getMessage() { return message; }
}