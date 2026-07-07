package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Schema(name = "Usuario")
public class UsuarioDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "El username es obligatorio")
    @Schema(example = "usuario1")
    private String username;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(example = "Maria")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(example = "Martinez")
    private String apellido;

    @Email(message = "El email debe tener un formato valido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(example = "usuario1@minimarket.cl")
    private String email;

    @NotBlank(message = "La direccion es obligatoria")
    @Schema(example = "Av. Principal 123")
    private String direccion;

    @NotBlank(message = "La password es obligatoria")
    @Schema(example = "Password123*")
    private String password;

    @Schema(example = "[1,2]")
    private Set<Long> rolIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getRolIds() {
        return rolIds;
    }

    public void setRolIds(Set<Long> rolIds) {
        this.rolIds = rolIds;
    }
}
