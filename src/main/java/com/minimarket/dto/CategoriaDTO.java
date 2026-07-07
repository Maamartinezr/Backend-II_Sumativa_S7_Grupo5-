package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Categoria")
public class CategoriaDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Schema(example = "Abarrotes")
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
