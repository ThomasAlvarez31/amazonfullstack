package com.ejemplo.productservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void constructorYGetters_deberianFuncionarCorrectamente() {
        Product p = new Product("Teclado", 49.99);

        assertEquals("Teclado", p.getName());
        assertEquals(49.99, p.getPrice());
        assertNull(p.getId());
    }

    @Test
    void setters_deberianActualizarCampos() {
        Product p = new Product();
        p.setId(5L);
        p.setName("Mouse");
        p.setPrice(29.99);

        assertEquals(5L, p.getId());
        assertEquals("Mouse", p.getName());
        assertEquals(29.99, p.getPrice());
    }
}
