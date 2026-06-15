package com.ecommerce.notifications.kafka;

import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;

public class NotificationConsumer {

    private final NotificationService service;

    public NotificationConsumer(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(topics = "notifications", groupId = "notifications-group")
    public void consume(String message) {
        Notification notification = new Notification();
        notification.setType("KAFKA");
        notification.setMessage(message);
        notification.setStatus("RECIBIDO");
        service.save(notification);
    }
}
