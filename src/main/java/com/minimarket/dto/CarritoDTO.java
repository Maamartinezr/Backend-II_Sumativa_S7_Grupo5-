package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "Carrito")
public class CarritoDTO {

    @Schema(example = "1")
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El usuario debe ser valido")
    @Schema(example = "1")
    private Long usuarioId;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El producto debe ser valido")
    @Schema(example = "1")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Schema(example = "2")
    private Integer cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
