package com.ecommerce.auth.controller;

import org.springframework.web.bind.annotation.*;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){

        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){

        return authService.login(user.getEmail(), user.getPassword());
    }
}