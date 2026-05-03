package git.meet_base.notification_ms.infrastructure.messaging;

import git.meet_base.notification_ms.client.AuthClient;
import git.meet_base.notification_ms.infrastructure.email.EmailService;
import git.meet_base.notification_ms.infrastructure.messaging.dto.MeetEventPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetEventConsumer {

    private final EmailService emailService;
    private final AuthClient authClient;

    public MeetEventConsumer(EmailService emailService, AuthClient authClient) {
        this.emailService = emailService;
        this.authClient = authClient;
    }

    @KafkaListener(
            topics = "meet-notifications-topic",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMeetEvent( @Payload MeetEventPayload payload,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                  @Header(KafkaHeaders.OFFSET) long offset,
                                  Acknowledgment acknowledgment) {
        System.out.println("KAFKA EVENT RECEIVED: \n" + payload.getMessage());

        List<String> targetEmails;

        try {

            if (payload.getTargetUserIds() == null || payload.getTargetUserIds().isEmpty()) {
                System.out.println("KAFKA EVENT FOR MANAGERS COMPANY: \n" + payload.getCompanyId());
                targetEmails = authClient.getActiveManagerEmails(payload.getCompanyId());
            }else {

                targetEmails = authClient.getUserEmailsBatch(payload.getTargetUserIds());
            }
            String subject = "MeetBase Notification: " + payload.getAction().replace("_", " ");

            if (targetEmails != null && !targetEmails.isEmpty()) {
                for (String email : targetEmails) {
                    System.out.println("Sending email to: " + email);
                    emailService.sendEmail(email, subject, payload.getMessage());
                    acknowledgment.acknowledge();
                }
            } else {
                System.out.println("No users found to notify for this event.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}