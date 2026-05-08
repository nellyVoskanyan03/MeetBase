package git.meet_base.notification_ms.infrastructure.messaging;

import git.meet_base.notification_ms.client.AuthClient;
import git.meet_base.notification_ms.infrastructure.email.EmailService;
import git.meet_base.notification_ms.infrastructure.messaging.dto.MeetEventPayload;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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

    private final Counter failedEmailFetchCounter;
    private final Counter failedNotificationSendCounter;

    public MeetEventConsumer(EmailService emailService, AuthClient authClient, MeterRegistry meterRegistry) {
        this.emailService = emailService;
        this.authClient = authClient;

        this.failedEmailFetchCounter = meterRegistry.counter("business.failed.email.fetch");
        this.failedNotificationSendCounter = meterRegistry.counter("business.failed.notification.send");
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
            try {
                if (payload.getTargetUserIds() == null || payload.getTargetUserIds().isEmpty()) {
                    System.out.println("KAFKA EVENT FOR MANAGERS COMPANY: \n" + payload.getCompanyId());
                    targetEmails = authClient.getActiveManagerEmails(payload.getCompanyId());
                } else {
                    targetEmails = authClient.getUserEmailsBatch(payload.getTargetUserIds());
                }
            } catch (Exception e) {
                failedEmailFetchCounter.increment();
                System.err.println("Failed to fetch emails from Auth Service: " + e.getMessage());
                return;
            }

            String subject = "MeetBase Notification: " + payload.getAction().replace("_", " ");

            if (targetEmails != null && !targetEmails.isEmpty()) {
                for (String email : targetEmails) {
                    System.out.println("Sending email to: " + email);
                    try {
                        emailService.sendEmail(email, subject, payload.getMessage());
                    } catch (Exception e) {
                        failedNotificationSendCounter.increment();
                        System.err.println("Failed to send email to " + email + ": " + e.getMessage());
                    }
                }
            } else {
                System.out.println("No users found to notify for this event.");
            }

            acknowledgment.acknowledge();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}