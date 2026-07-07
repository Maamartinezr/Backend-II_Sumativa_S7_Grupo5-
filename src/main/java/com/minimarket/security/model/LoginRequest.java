package com.minimarket.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    @Schema(description = "Nombre de usuario registrado", example = "admin")
    private String username;

    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "Admin123*")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
