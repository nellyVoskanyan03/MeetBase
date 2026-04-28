package git.meet_base.notification_ms.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public MeetEventConsumer(EmailService emailService) {
        this.emailService = emailService;
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

        try {

            if (payload.getTargetUserIds() == null || payload.getTargetUserIds().isEmpty()) {
                System.out.println("KAFKA EVENT FOR MANAGERS COMPANY: \n" + payload.getCompanyId());
                //TODO: call login_ms to get the manager emails and send the emails
                return;
            }
            String subject = "MeetBase Notification: " + payload.getAction().replace("_", " ");

            //TODO: call login_ms to get the target emails and send the emails
            List<String> emails = List.of("");

            for (String email : emails) {
                System.out.println("email: \n" + email);
                emailService.sendEmail(email, subject, payload.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO: send acknowledgment to kafka(if there is an authid_ms do it there)
        //acknowledgment.acknowledge();
    }
}