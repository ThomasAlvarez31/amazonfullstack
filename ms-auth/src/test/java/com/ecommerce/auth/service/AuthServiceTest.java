package com.ecommerce.auth.service;

import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_deberiaHashearPasswordYGuardarUsuario() {
        User user = new User("test@mail.com", "123456", null);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hash_seguro");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("token.jwt.generado");

        Map<String, String> resultado = authService.register(user);

        assertEquals("token.jwt.generado", resultado.get("token"));
        assertEquals("USER", resultado.get("role"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_deberiaLanzarExcepcionSiCorreoYaExiste() {
        User user = new User("test@mail.com", "123456", null);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(user));

        assertEquals("El correo ya esta registrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_deberiaRetornarTokenYRolCuandoCredencialesCorrectas() {
        User user = new User("test@mail.com", "hash_seguro", "USER");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hash_seguro")).thenReturn(true);
        when(jwtService.generateToken("test@mail.com", "USER")).thenReturn("token.jwt.generado");

        Map<String, String> resultado = authService.login("test@mail.com", "123456");

        assertEquals("token.jwt.generado", resultado.get("token"));
        assertEquals("USER", resultado.get("role"));
        verify(jwtService, times(1)).generateToken("test@mail.com", "USER");
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("noexiste@mail.com", "123456"));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoPasswordEsIncorrecta() {
        User user = new User("test@mail.com", "hash_seguro", "USER");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("incorrecta", "hash_seguro")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("test@mail.com", "incorrecta"));

        assertEquals("Credenciales invalidas", ex.getMessage());
    }
}
