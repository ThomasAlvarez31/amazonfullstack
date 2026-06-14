package com.ecommerce.notifications.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void settersYGetters_deberianFuncionarCorrectamente() {
        Notification n = new Notification();
        n.setId(1L);
        n.setUserId(2L);
        n.setType("EMAIL");
        n.setMessage("Hola");
        n.setStatus("UNREAD");

        assertEquals(1L, n.getId());
        assertEquals(2L, n.getUserId());
        assertEquals("EMAIL", n.getType());
        assertEquals("Hola", n.getMessage());
        assertEquals("UNREAD", n.getStatus());
    }
}
