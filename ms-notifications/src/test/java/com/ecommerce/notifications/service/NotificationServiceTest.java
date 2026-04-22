package com.ecommerce.notifications.service;

import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    @Test
    void findByUserId_deberiaRetornarNotificacionesDelUsuario() {
        Notification n = new Notification();
        n.setUserId(1L);
        n.setType("EMAIL");
        n.setMessage("Tu pedido fue enviado");
        n.setStatus("ENVIADO");

        when(repository.findByUserId(1L)).thenReturn(List.of(n));

        List<Notification> resultado = service.findByUserId(1L);

        assertEquals(1, resultado.size());
        assertEquals("EMAIL", resultado.get(0).getType());
        verify(repository, times(1)).findByUserId(1L);
    }

    @Test
    void save_deberiaGuardarYRetornarNotificacion() {
        Notification n = new Notification();
        n.setUserId(2L);
        n.setType("SMS");
        n.setMessage("Tu pago fue procesado");
        n.setStatus("PENDIENTE");

        when(repository.save(any(Notification.class))).thenReturn(n);

        Notification resultado = service.save(n);

        assertEquals("SMS", resultado.getType());
        assertEquals("PENDIENTE", resultado.getStatus());
        verify(repository, times(1)).save(n);
    }

    @Test
    void delete_deberiaEliminarYRetornarTrueCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean resultado = service.delete(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_deberiaRetornarFalseCuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean resultado = service.delete(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
