package se.mojujo.auditservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.mojujo.auditservice.config.RabbitConfig;
import se.mojujo.auditservice.entity.AuditEvent;
import se.mojujo.auditservice.repository.AuditEventRepository;
import se.mojujo.auditservice.util.LogUtil;

import java.time.Instant;
import java.util.Map;

@Service
public class AuditEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventListener.class);

    private final AuditEventRepository auditEventRepository;

    @Autowired
    public AuditEventListener(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @RabbitListener(queues = RabbitConfig.AUDIT_QUEUE)
    public void handleAuditEvent(Map<String, Object> message) {

        try {
            String eventType = (String) message.get("eventType");
            Object data = message.get("data");
            Instant timestamp = (Instant) message.get("timestamp");

            // Convert data to JSON
            String jsonData = new ObjectMapper().writeValueAsString(data);

            AuditEvent auditEvent = new AuditEvent(eventType, jsonData, timestamp);

            auditEventRepository.save(auditEvent);

            LogUtil.info(logger,
                    "AUDIT_EVENT_SAVED",
                    "Audit event saved successfully",
                    "eventType", eventType, "timestamp", timestamp.toString());

        } catch (Exception e) {
            LogUtil.error(logger,
                    "AUDIT_EVENT_FAILED",
                    "Failed to process audit event",
                    "error", e.getMessage(), "message", message);
        }
    }
}
