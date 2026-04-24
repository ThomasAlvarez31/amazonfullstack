package com.ecommerce.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "El correo debe tener un formato valido (ej: usuario@dominio.com)")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    private String password;

    private String role;

    public User() {}

    public User(String email, String password, String role) {
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    public Long getId()                    { return id; }
    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }
    public String getPassword()            { return password; }
    public void setPassword(String pass)   { this.password = pass; }
    public String getRole()                { return role; }
    public void setRole(String role)       { this.role = role; }
}
