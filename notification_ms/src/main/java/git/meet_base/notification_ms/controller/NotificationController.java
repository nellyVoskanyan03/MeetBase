package git.meet_base.notification_ms.controller;

import git.meet_base.notification_ms.infrastructure.email.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "Test Controller", description = "Testing email notifications")
public class NotificationController {
    private final EmailService emailService;
    public NotificationController(EmailService emailService){
        this.emailService = emailService;
    }

    //Test endpoint
    @GetMapping("/send-test-email")
    public String sendEmail(){
        emailService.sendEmail("","email test","this test email");
        return "ola";
    }
}
