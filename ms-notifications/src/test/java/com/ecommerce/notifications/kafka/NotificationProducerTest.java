package com.ecommerce.notifications.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private NotificationProducer producer;

    @Test
    void send_deberiaPublicarMensajeEnTopicNotifications() {
        String mensaje = "test-message";

        producer.send(mensaje);

        verify(kafkaTemplate, times(1)).send("notifications", mensaje);
    }
}
