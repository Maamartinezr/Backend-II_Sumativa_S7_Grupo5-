package com.minimarket.mapper;

import com.minimarket.dto.CarritoDTO;
import com.minimarket.dto.ProductoDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;

public final class EntityDtoMapper {

    private EntityDtoMapper() {
    }

    public static ProductoDTO toProductoDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null);
        return dto;
    }

    public static Producto toProductoEntity(ProductoDTO dto, Categoria categoria) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);
        return producto;
    }

    public static CarritoDTO toCarritoDto(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuario() != null ? carrito.getUsuario().getId() : null);
        dto.setProductoId(carrito.getProducto() != null ? carrito.getProducto().getId() : null);
        dto.setCantidad(carrito.getCantidad());
        return dto;
    }

    public static Carrito toCarritoEntity(CarritoDTO dto, Usuario usuario, Producto producto) {
        Carrito carrito = new Carrito();
        carrito.setId(dto.getId());
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(dto.getCantidad());
        return carrito;
    }
}
