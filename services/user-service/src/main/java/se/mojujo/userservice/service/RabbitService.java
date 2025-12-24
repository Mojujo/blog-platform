package se.mojujo.userservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.config.RabbitConfig;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuditEvent(String eventType, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", eventType);
        payload.put("data", data);
        payload.put("timestamp", Instant.now().toString());

        rabbitTemplate.convertAndSend(
                RabbitConfig.AUDIT_EXCHANGE,
                RabbitConfig.AUDIT_ROUTING_KEY,
                payload
        );
    }

    public void sendUsernameChangedEvent(UUID userId, String newUsername) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("newUsername", newUsername);

        rabbitTemplate.convertAndSend(
                RabbitConfig.USERNAME_CHANGED_EXCHANGE,
                RabbitConfig.USERNAME_CHANGED_ROUTING_KEY,
                payload
        );
    }
}
