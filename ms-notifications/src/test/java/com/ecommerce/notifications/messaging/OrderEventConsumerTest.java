package com.ecommerce.notifications.messaging;

import com.ecommerce.notifications.event.OrderCreatedEvent;
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
class OrderEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderEventConsumer consumer;

    @Test
    void handleOrderCreated_deberiaCrearNotificacionParaElUsuario() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        event.setUserId(5L);
        event.setProductId(10L);
        event.setQuantity(3);
        event.setTotalPrice(150.0);
        event.setStatus("PENDIENTE");

        consumer.handleOrderCreated(event);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(1)).save(captor.capture());

        Notification saved = captor.getValue();
        assertEquals(5L, saved.getUserId());
        assertEquals("ORDER_CREATED", saved.getType());
        assertEquals("UNREAD", saved.getStatus());
        assertTrue(saved.getMessage().contains("#1"));
        assertTrue(saved.getMessage().contains("150,00").replace(",", ".") ||
                   saved.getMessage().contains("150.00"));
    }

    @Test
    void handleOrderCreated_deberiaIncluirCantidadEnMensaje() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(2L);
        event.setUserId(3L);
        event.setQuantity(5);
        event.setTotalPrice(250.0);
        event.setStatus("PAGADO");

        consumer.handleOrderCreated(event);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService).save(captor.capture());
        String msg = captor.getValue().getMessage();
        assertTrue(msg.contains("5"));
    }
}
