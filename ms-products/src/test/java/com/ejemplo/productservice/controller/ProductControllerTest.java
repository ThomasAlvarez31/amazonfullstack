package com.ejemplo.productservice.controller;

import com.ejemplo.productservice.model.Product;
import com.ejemplo.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProducts_deberiaRetornarListaDeProductos() {
        Product product = new Product("Laptop", 999.99);

        when(productService.findAll()).thenReturn(List.of(product));

        List<Product> resultado = productController.getAllProducts();

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getName());
        assertEquals(999.99, resultado.get(0).getPrice());
        verify(productService, times(1)).findAll();
    }

    @Test
    void createProduct_deberiaGuardarYRetornarProducto() {
        Product product = new Product("Teclado", 49.99);

        when(productService.save(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Teclado", response.getBody().getName());
        assertEquals(49.99, response.getBody().getPrice());
        verify(productService, times(1)).save(product);
    }
}
