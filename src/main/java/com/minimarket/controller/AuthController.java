package com.minimarket.controller;

import com.minimarket.security.model.AuthResponse;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de autenticación JWT")
public class AuthController {

    private static final String LOGIN_REQUEST_EXAMPLE = """
            {
              "username": "admin",
              "password": "Admin123*"
            }
            """;
    private static final String LOGIN_RESPONSE_EXAMPLE = """
            {
              "token": "jwt-token-ejemplo",
              "tokenType": "Bearer",
              "expiresIn": 3600000
            }
            """;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica credenciales y retorna un token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = LOGIN_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Credenciales de acceso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(value = LOGIN_REQUEST_EXAMPLE)))
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", expirationMs));
    }
}
