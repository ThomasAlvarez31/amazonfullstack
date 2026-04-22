package com.ecommerce.inventory.service;

import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
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
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryService service;

    @Test
    void findAll_deberiaRetornarListaDeInventarios() {
        Inventory item = new Inventory();
        item.setProductId(1L);
        item.setQuantity(10);
        item.setLocation("Bodega A");

        when(repository.findAll()).thenReturn(List.of(item));

        List<Inventory> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getProductId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarInventarioCuandoExiste() {
        Inventory item = new Inventory();
        item.setProductId(2L);
        item.setQuantity(5);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Inventory> resultado = service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(2L, resultado.get().getProductId());
    }

    @Test
    void save_deberiaGuardarYRetornarInventario() {
        Inventory item = new Inventory();
        item.setProductId(3L);
        item.setQuantity(20);
        item.setLocation("Bodega B");

        when(repository.save(any(Inventory.class))).thenReturn(item);

        Inventory resultado = service.save(item);

        assertEquals(20, resultado.getQuantity());
        verify(repository, times(1)).save(item);
    }

    @Test
    void update_deberiaActualizarCuandoExiste() {
        Inventory existing = new Inventory();
        existing.setProductId(1L);
        existing.setQuantity(5);
        existing.setLocation("Bodega A");

        Inventory updated = new Inventory();
        updated.setProductId(1L);
        updated.setQuantity(15);
        updated.setLocation("Bodega C");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Inventory.class))).thenReturn(existing);

        Optional<Inventory> resultado = service.update(1L, updated);

        assertTrue(resultado.isPresent());
        assertEquals(15, resultado.get().getQuantity());
        assertEquals("Bodega C", resultado.get().getLocation());
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
