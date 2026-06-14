package com.ecommerce.users.service;

import com.ecommerce.users.model.User;
import com.ecommerce.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio de negocio para la gestión de usuarios. Aplica hash de contraseñas con BCrypt.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public List<User> findAll() { 
        return userRepository.findAll(); 
    }

    public User save(User user) {

        String passwordHasheada = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHasheada);
        
        return userRepository.save(user);
    }

    public void delete(Long id) { 
        userRepository.deleteById(id); 
    }
}