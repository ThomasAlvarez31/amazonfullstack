package com.ecommerce.notifications.kafka;

import org.springframework.kafka.core.KafkaTemplate;

public class NotificationProducer {

    private static final String TOPIC = "notifications";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
