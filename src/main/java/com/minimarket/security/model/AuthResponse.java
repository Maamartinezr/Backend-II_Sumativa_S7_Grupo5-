package com.minimarket.security.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {

    @Schema(description = "Token JWT de acceso")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Tiempo de expiración en milisegundos", example = "3600000")
    private long expiresIn;

    public AuthResponse(String token, String tokenType, long expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
