package com.ecommerce.orders.messaging;

import com.ecommerce.orders.config.RabbitMQConfig;
import com.ecommerce.orders.event.OrderCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderEventPublisher publisher;

    @Test
    void publishOrderCreated_deberiaEnviarEventoAlExchange() {
        OrderCreatedEvent event = new OrderCreatedEvent(1L, 2L, 3L, 2, 99.99, "PENDIENTE");

        publisher.publishOrderCreated(event);

        verify(rabbitTemplate, times(1)).convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            event
        );
    }

    @Test
    void publishOrderCreated_deberiaFuncionarConEventoVacio() {
        OrderCreatedEvent event = new OrderCreatedEvent();

        publisher.publishOrderCreated(event);

        verify(rabbitTemplate, times(1)).convertAndSend(
            eq(RabbitMQConfig.EXCHANGE),
            eq(RabbitMQConfig.ROUTING_KEY),
            any(OrderCreatedEvent.class)
        );
    }
}
