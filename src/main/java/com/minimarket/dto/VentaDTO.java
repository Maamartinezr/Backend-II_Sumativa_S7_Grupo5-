package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;
import java.util.List;

@Schema(name = "Venta")
public class VentaDTO {

    @Schema(example = "1")
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El usuario debe ser valido")
    @Schema(example = "1")
    private Long usuarioId;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(example = "2026-07-06T04:00:00.000+00:00")
    private Date fecha;

    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @Schema(example = "3000.0")
    private Double total;

    @Schema(example = "[1,2]")
    private List<Long> detalleIds;

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<Long> getDetalleIds() {
        return detalleIds;
    }

    public void setDetalleIds(List<Long> detalleIds) {
        this.detalleIds = detalleIds;
    }
}
