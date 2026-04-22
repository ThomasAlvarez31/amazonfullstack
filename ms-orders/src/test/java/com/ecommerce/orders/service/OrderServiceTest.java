package com.ecommerce.orders.service;

import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    @Test
    void findAll_deberiaRetornarListaDeOrdenes() {
        Order order = new Order();
        order.setUserId(1L);
        order.setProductId(10L);
        order.setQuantity(2);
        order.setStatus("PENDIENTE");
        order.setTotalPrice(199.99);

        when(repository.findAll()).thenReturn(List.of(order));

        List<Order> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getStatus());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarOrdenCuandoExiste() {
        Order order = new Order();
        order.setUserId(1L);
        order.setStatus("PAGADO");

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> resultado = service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("PAGADO", resultado.get().getStatus());
    }

    @Test
    void save_deberiaGuardarYRetornarOrden() {
        Order order = new Order();
        order.setUserId(2L);
        order.setProductId(5L);
        order.setQuantity(1);
        order.setStatus("PENDIENTE");
        order.setTotalPrice(50.0);

        when(repository.save(any(Order.class))).thenReturn(order);

        Order resultado = service.save(order);

        assertEquals("PENDIENTE", resultado.getStatus());
        assertEquals(50.0, resultado.getTotalPrice());
        verify(repository, times(1)).save(order);
    }

    @Test
    void update_deberiaActualizarCuandoExiste() {
        Order existing = new Order();
        existing.setStatus("PENDIENTE");
        existing.setTotalPrice(100.0);

        Order updated = new Order();
        updated.setUserId(1L);
        updated.setProductId(2L);
        updated.setQuantity(3);
        updated.setStatus("ENVIADO");
        updated.setTotalPrice(300.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Order.class))).thenReturn(existing);

        Optional<Order> resultado = service.update(1L, updated);

        assertTrue(resultado.isPresent());
        assertEquals("ENVIADO", resultado.get().getStatus());
        assertEquals(300.0, resultado.get().getTotalPrice());
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
