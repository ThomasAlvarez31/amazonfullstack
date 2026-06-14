package com.ecommerce.orders.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedEventTest {

    @Test
    void constructorCompleto_deberiaAsignarTodosLosCampos() {
        OrderCreatedEvent event = new OrderCreatedEvent(1L, 2L, 3L, 4, 99.9, "PENDIENTE");

        assertEquals(1L, event.getOrderId());
        assertEquals(2L, event.getUserId());
        assertEquals(3L, event.getProductId());
        assertEquals(4, event.getQuantity());
        assertEquals(99.9, event.getTotalPrice());
        assertEquals("PENDIENTE", event.getStatus());
    }

    @Test
    void setters_deberianActualizarCampos() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(10L);
        event.setUserId(20L);
        event.setProductId(30L);
        event.setQuantity(5);
        event.setTotalPrice(250.0);
        event.setStatus("PAGADO");

        assertEquals(10L, event.getOrderId());
        assertEquals(20L, event.getUserId());
        assertEquals(30L, event.getProductId());
        assertEquals(5, event.getQuantity());
        assertEquals(250.0, event.getTotalPrice());
        assertEquals("PAGADO", event.getStatus());
    }
}
