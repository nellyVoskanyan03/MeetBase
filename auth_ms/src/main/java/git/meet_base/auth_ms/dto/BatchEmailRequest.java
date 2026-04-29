package git.meet_base.auth_ms.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public class BatchEmailRequest {

    @NotEmpty(message = "User ID list cannot be empty")
    private List<UUID> userIds;

    public BatchEmailRequest() {}

    public BatchEmailRequest(List<UUID> userIds) {
        this.userIds = userIds;
    }

    public List<UUID> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<UUID> userIds) {
        this.userIds = userIds;
    }
}