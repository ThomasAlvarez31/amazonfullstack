package com.ecommerce.orders.messaging;

import com.ecommerce.orders.config.RabbitMQConfig;
import com.ecommerce.orders.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de órdenes en RabbitMQ para comunicación asíncrona
 * con ms-notifications y otros consumidores.
 */
@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica un evento de orden creada al exchange de RabbitMQ.
     *
     * @param event datos del evento de la orden
     */
    public void publishOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            event
        );
    }
}
