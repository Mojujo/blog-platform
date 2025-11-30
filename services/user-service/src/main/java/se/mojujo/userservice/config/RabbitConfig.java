package se.mojujo.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitConfig {

    public static final String AUDIT_QUEUE = "audit-queue";
    public static final String NOTIFICATION_QUEUE = "notification-queue";

    public static final String AUDIT_EXCHANGE = "audit-exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification-exchange";

    public static final String AUDIT_ROUTING_KEY = "audit.routing";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routing";

    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public DirectExchange auditExchange() {
        return new DirectExchange(AUDIT_EXCHANGE);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, DirectExchange auditExchange) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(AUDIT_ROUTING_KEY);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
