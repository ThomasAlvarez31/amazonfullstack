package com.ecommerce.shipping.service;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.repository.ShipmentRepository;
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
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository repository;

    @InjectMocks
    private ShipmentService service;

    @Test
    void findAll_deberiaRetornarListaDeEnvios() {
        Shipment shipment = new Shipment();
        shipment.setOrderId(1L);
        shipment.setStatus("EN_CAMINO");
        shipment.setCarrier("Chilexpress");

        when(repository.findAll()).thenReturn(List.of(shipment));

        List<Shipment> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("EN_CAMINO", resultado.get(0).getStatus());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findByOrderId_deberiaRetornarEnviosDeOrden() {
        Shipment shipment = new Shipment();
        shipment.setOrderId(1L);
        shipment.setStatus("ENTREGADO");

        when(repository.findByOrderId(1L)).thenReturn(List.of(shipment));

        List<Shipment> resultado = service.findByOrderId(1L);

        assertEquals(1, resultado.size());
        assertEquals("ENTREGADO", resultado.get(0).getStatus());
    }

    @Test
    void save_deberiaGuardarYRetornarEnvio() {
        Shipment shipment = new Shipment();
        shipment.setOrderId(2L);
        shipment.setAddress("Av. Siempre Viva 123");
        shipment.setStatus("PENDIENTE");
        shipment.setCarrier("Starken");

        when(repository.save(any(Shipment.class))).thenReturn(shipment);

        Shipment resultado = service.save(shipment);

        assertEquals("PENDIENTE", resultado.getStatus());
        assertEquals("Starken", resultado.getCarrier());
        verify(repository, times(1)).save(shipment);
    }

    @Test
    void update_deberiaActualizarCuandoExiste() {
        Shipment existing = new Shipment();
        existing.setStatus("PENDIENTE");
        existing.setCarrier("Chilexpress");

        Shipment updated = new Shipment();
        updated.setAddress("Nueva Calle 456");
        updated.setStatus("EN_CAMINO");
        updated.setCarrier("Starken");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Shipment.class))).thenReturn(existing);

        Optional<Shipment> resultado = service.update(1L, updated);

        assertTrue(resultado.isPresent());
        assertEquals("EN_CAMINO", resultado.get().getStatus());
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
