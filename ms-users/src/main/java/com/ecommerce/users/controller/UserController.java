package com.ecommerce.users.controller;

import com.ecommerce.users.model.User;
import com.ecommerce.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de usuarios.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Users", description = "Gestión de usuarios")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping
    public java.util.List<User> getAll() {
        return userService.findAll();
    }
}