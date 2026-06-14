package com.ejemplo.productservice.service;

import com.ejemplo.productservice.model.Product;
import com.ejemplo.productservice.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService service;

    private Product buildProduct() {
        Product p = new Product("Laptop", 999.0);
        p.setId(1L);
        return p;
    }

    @Test
    void findAll_deberiaRetornarListaDeProductos() {
        when(productRepository.findAll()).thenReturn(List.of(buildProduct()));

        List<Product> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarProductoCuandoExiste() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(buildProduct()));

        Optional<Product> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
    }

    @Test
    void findById_deberiaRetornarVacioCuandoNoExiste() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = service.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void save_deberiaGuardarProducto() {
        Product p = buildProduct();
        when(productRepository.save(any(Product.class))).thenReturn(p);

        Product result = service.save(p);

        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(p);
    }

    @Test
    void update_deberiaActualizarProducto() {
        Product updated = new Product("Monitor", 299.0);
        updated.setId(1L);
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        Product result = service.update(1L, updated);

        assertEquals("Monitor", result.getName());
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(updated);
    }

    @Test
    void delete_deberiaEliminarYRetornarTrueCuandoExiste() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_deberiaRetornarFalseCuandoNoExiste() {
        when(productRepository.existsById(99L)).thenReturn(false);

        boolean result = service.delete(99L);

        assertFalse(result);
        verify(productRepository, never()).deleteById(any());
    }
}
