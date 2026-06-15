package com.ecommerce.auth.service;

import com.ecommerce.auth.model.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Servicio de autenticación y registro con JWT firmado (RS256).
 * Tokens con fecha de expiración configurada en JwtService.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.jwtService      = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario, hashea su contraseña con BCrypt y retorna un JWT.
     *
     * @param user datos del usuario (email, password)
     * @return mapa con {@code token} JWT y {@code role} asignado
     * @throws RuntimeException si el correo ya está registrado
     */
    public Map<String, String> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya esta registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), "USER");
        return Map.of("token", token, "role", "USER");
    }

    /**
     * Autentica un usuario por email y contraseña y retorna un JWT firmado.
     *
     * @param email    correo del usuario
     * @param password contraseña en texto plano
     * @return mapa con {@code token} JWT y {@code role} del usuario
     * @throws RuntimeException si el usuario no existe o las credenciales son inválidas
     */
    public Map<String, String> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return Map.of("token", token, "role", user.getRole());
    }
}
