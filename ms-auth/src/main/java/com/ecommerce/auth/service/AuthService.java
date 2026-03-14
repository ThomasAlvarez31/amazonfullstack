package com.ecommerce.auth.service;

import org.springframework.stereotype.Service;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService = new JwtService();

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User register(User user){

        return userRepository.save(user);
    }

    public String login(String email, String password){

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email);
    }
}