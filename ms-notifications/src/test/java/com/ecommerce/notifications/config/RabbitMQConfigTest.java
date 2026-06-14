package com.ecommerce.notifications.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    void constante_deberiaSerLaColaCorrecta() {
        assertEquals("orders.notifications.queue", RabbitMQConfig.QUEUE);
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
