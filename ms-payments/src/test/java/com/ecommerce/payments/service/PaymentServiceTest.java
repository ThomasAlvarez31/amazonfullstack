package com.ecommerce.payments.service;

import com.ecommerce.payments.model.Payment;
import com.ecommerce.payments.repository.PaymentRepository;
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
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentService service;

    @Test
    void findAll_deberiaRetornarListaDePagos() {
        Payment payment = new Payment();
        payment.setOrderId(1L);
        payment.setAmount(150.0);
        payment.setStatus("COMPLETADO");
        payment.setPaymentMethod("TARJETA");

        when(repository.findAll()).thenReturn(List.of(payment));

        List<Payment> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("COMPLETADO", resultado.get(0).getStatus());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarPagoCuandoExiste() {
        Payment payment = new Payment();
        payment.setOrderId(2L);
        payment.setAmount(200.0);
        payment.setStatus("PENDIENTE");

        when(repository.findById(1L)).thenReturn(Optional.of(payment));

        Optional<Payment> resultado = service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("PENDIENTE", resultado.get().getStatus());
    }

    @Test
    void save_deberiaGuardarYRetornarPago() {
        Payment payment = new Payment();
        payment.setOrderId(3L);
        payment.setAmount(99.99);
        payment.setStatus("PENDIENTE");
        payment.setPaymentMethod("EFECTIVO");

        when(repository.save(any(Payment.class))).thenReturn(payment);

        Payment resultado = service.save(payment);

        assertEquals(99.99, resultado.getAmount());
        assertEquals("EFECTIVO", resultado.getPaymentMethod());
        verify(repository, times(1)).save(payment);
    }

    @Test
    void update_deberiaActualizarCuandoExiste() {
        Payment existing = new Payment();
        existing.setOrderId(1L);
        existing.setAmount(50.0);
        existing.setStatus("PENDIENTE");
        existing.setPaymentMethod("TARJETA");

        Payment updated = new Payment();
        updated.setOrderId(1L);
        updated.setAmount(75.0);
        updated.setStatus("COMPLETADO");
        updated.setPaymentMethod("TRANSFERENCIA");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Payment.class))).thenReturn(existing);

        Optional<Payment> resultado = service.update(1L, updated);

        assertTrue(resultado.isPresent());
        assertEquals("COMPLETADO", resultado.get().getStatus());
        assertEquals(75.0, resultado.get().getAmount());
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
