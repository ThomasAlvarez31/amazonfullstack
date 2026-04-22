package com.ecommerce.users.service;

import com.ecommerce.users.model.User;
import com.ecommerce.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_deberiaRetornarListaDeUsuarios() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> resultado = userService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("test@mail.com", resultado.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void save_deberiaHashearPasswordAntesDeGuardar() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("123456");

        when(passwordEncoder.encode("123456")).thenReturn("hash_seguro");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User resultado = userService.save(user);

        assertEquals("hash_seguro", resultado.getPassword());
        verify(passwordEncoder, times(1)).encode("123456");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void delete_deberiaLlamarDeleteByIdConElIdCorrecto() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
