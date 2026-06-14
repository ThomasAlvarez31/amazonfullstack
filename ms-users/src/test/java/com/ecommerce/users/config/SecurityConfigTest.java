package com.ecommerce.users.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig config = new SecurityConfig();

    @Test
    void passwordEncoder_deberiaCrearEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void passwordEncoder_deberiaHashearPassword() {
        PasswordEncoder encoder = config.passwordEncoder();
        String hash = encoder.encode("mi-password");
        assertNotNull(hash);
        assertNotEquals("mi-password", hash);
    }
}
