package com.ecommerce.notifications.kafka;

import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationService service;

    @InjectMocks
    private NotificationConsumer consumer;

    @Test
    void consume_deberiaGuardarNotificacionDeKafka() {
        String mensaje = "Pedido recibido";

        consumer.consume(mensaje);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(service, times(1)).save(captor.capture());

        Notification saved = captor.getValue();
        assertEquals("KAFKA", saved.getType());
        assertEquals(mensaje, saved.getMessage());
        assertEquals("RECIBIDO", saved.getStatus());
    }
}
