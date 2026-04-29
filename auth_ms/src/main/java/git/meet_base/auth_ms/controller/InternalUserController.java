package git.meet_base.auth_ms.controller;

import git.meet_base.auth_ms.dto.BatchEmailRequest;
import git.meet_base.auth_ms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<UUID, String>> getEmailsBatch(@Valid @RequestBody BatchEmailRequest request) {
        Map<UUID, String> emailMap = userService.getUserEmailsBatch(request.getUserIds());
        return ResponseEntity.ok(emailMap);
    }
}