package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

@Schema(name = "Inventario")
public class InventarioDTO {

    @Schema(example = "1")
    private Long id;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El producto debe ser valido")
    @Schema(example = "1")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Schema(example = "5")
    private Integer cantidad;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Schema(example = "ENTRADA")
    private String tipoMovimiento;

    @NotNull(message = "La fecha del movimiento es obligatoria")
    @Schema(example = "2026-07-06T04:00:00.000+00:00")
    private Date fechaMovimiento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}
