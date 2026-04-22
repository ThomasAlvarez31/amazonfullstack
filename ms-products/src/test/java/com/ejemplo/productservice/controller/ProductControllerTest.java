package com.ejemplo.productservice.controller;

import com.ejemplo.productservice.model.Product;
import com.ejemplo.productservice.repository.ProductRepository;
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
class ProductControllerTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProducts_deberiaRetornarListaDeProductos() {
        Product product = new Product("Laptop", 999.99);

        when(repository.findAll()).thenReturn(List.of(product));

        List<Product> resultado = productController.getAllProducts();

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getName());
        assertEquals(999.99, resultado.get(0).getPrice());
        verify(repository, times(1)).findAll();
    }

    @Test
    void createProduct_deberiaGuardarYRetornarProducto() {
        Product product = new Product("Teclado", 49.99);

        when(repository.save(any(Product.class))).thenReturn(product);

        Product resultado = productController.createProduct(product);

        assertEquals("Teclado", resultado.getName());
        assertEquals(49.99, resultado.getPrice());
        verify(repository, times(1)).save(product);
    }
}
