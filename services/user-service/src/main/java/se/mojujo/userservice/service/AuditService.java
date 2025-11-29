package se.mojujo.userservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.config.RabbitConfig;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuditService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AuditService(RabbitTemplate rabbitTemplate) {
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
}
