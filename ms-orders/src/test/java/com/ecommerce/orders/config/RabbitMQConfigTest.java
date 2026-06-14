package com.ecommerce.orders.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {

    @Mock
    private ConnectionFactory connectionFactory;

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void constantes_deberianTenerValoresCorrectos() {
        assertEquals("orders.exchange", RabbitMQConfig.EXCHANGE);
        assertEquals("orders.notifications.queue", RabbitMQConfig.QUEUE);
        assertEquals("order.created", RabbitMQConfig.ROUTING_KEY);
    }

    @Test
    void ordersExchange_deberiaCrearTopicExchange() {
        TopicExchange exchange = config.ordersExchange();
        assertNotNull(exchange);
        assertEquals(RabbitMQConfig.EXCHANGE, exchange.getName());
    }

    @Test
    void ordersQueue_deberiaCrearColaDurable() {
        Queue queue = config.ordersQueue();
        assertNotNull(queue);
        assertEquals(RabbitMQConfig.QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void ordersBinding_deberiaVincularColaConExchange() {
        Queue queue = config.ordersQueue();
        TopicExchange exchange = config.ordersExchange();
        Binding binding = config.ordersBinding(queue, exchange);
        assertNotNull(binding);
        assertEquals(RabbitMQConfig.QUEUE, binding.getDestination());
        assertEquals(RabbitMQConfig.ROUTING_KEY, binding.getRoutingKey());
    }

    @Test
    void messageConverter_deberiaCrearJacksonConverter() {
        Jackson2JsonMessageConverter converter = config.messageConverter();
        assertNotNull(converter);
    }

    @Test
    void rabbitTemplate_deberiaCrearTemplateConConverter() {
        RabbitTemplate template = config.rabbitTemplate(connectionFactory);
        assertNotNull(template);
    }
}
