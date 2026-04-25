package git.meet_base.meet_ms.api.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MeetEventProducer {
    private static final String TOPIC = "meet-notifications-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MeetEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(MeetNotificationEvent event) {
        kafkaTemplate.send(TOPIC, event.getMeetId().toString(), event);
        System.out.println("KAFKA: Sent '" + event.getAction() + "' event to topic " + TOPIC);
    }
}