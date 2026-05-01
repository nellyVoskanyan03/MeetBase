package git.meet_base.auth_ms.controller;

import git.meet_base.auth_ms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users/emails")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    // Explicitly documenting this is for internal MS communication
    @PostMapping("/batch")
    public ResponseEntity<Map<UUID, String>> getEmailsBatch(@RequestBody List<UUID> userIds ) {
        Map<UUID, String> emailMap = userService.getUserEmailsBatch(userIds);
        return ResponseEntity.ok(emailMap);
    }
}