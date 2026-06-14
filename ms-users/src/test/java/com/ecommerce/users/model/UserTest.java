package com.ecommerce.users.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void settersYGetters_deberianFuncionarCorrectamente() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ismael");
        user.setEmail("ismael@test.com");
        user.setPassword("secret123");

        assertEquals(1L, user.getId());
        assertEquals("ismael", user.getUsername());
        assertEquals("ismael@test.com", user.getEmail());
        assertEquals("secret123", user.getPassword());
    }

    @Test
    void equals_deberiaSerTrueCuandoMismoObjeto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ismael");
        user.setEmail("ismael@test.com");
        user.setPassword("secret");

        assertEquals(user, user);
    }

    @Test
    void hashCode_deberiaSerConsistente() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ismael");

        int h1 = user.hashCode();
        int h2 = user.hashCode();

        assertEquals(h1, h2);
    }

    @Test
    void toString_deberiaRetornarString() {
        User user = new User();
        user.setUsername("ismael");

        assertNotNull(user.toString());
        assertTrue(user.toString().contains("ismael"));
    }
}
