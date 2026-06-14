package com.ecommerce.notifications.messaging;

import com.ecommerce.notifications.config.RabbitMQConfig;
import com.ecommerce.notifications.event.OrderCreatedEvent;
import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor RabbitMQ que recibe eventos de órdenes creadas desde ms-orders
 * y genera una notificación persistida en base de datos.
 */
@Component
public class OrderEventConsumer {

    private final NotificationService notificationService;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Escucha la cola {@code orders.notifications.queue} y crea una notificación
     * al usuario cuando su orden es procesada.
     *
     * @param event evento de orden creada recibido vía RabbitMQ
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setType("ORDER_CREATED");
        notification.setMessage(String.format(
            "Tu orden #%d fue recibida. Productos: %d, Total: $%.2f",
            event.getOrderId(), event.getQuantity(), event.getTotalPrice()
        ));
        notification.setStatus("UNREAD");
        notificationService.save(notification);
    }
}
