package com.minimarket.util;

import java.util.Set;

public final class MinimarketConstants {

    private MinimarketConstants() {
    }

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_CAJERO = "CAJERO";
    public static final String ROL_CLIENTE = "CLIENTE";
    public static final String ROL_EMPLEADO = "EMPLEADO";
    public static final String ROL_VENDEDOR = "VENDEDOR";

    public static final Set<String> ROLES_REGISTRO_VENTA = Set.of(ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR);
    public static final Set<String> ROLES_GESTION_INVENTARIO = Set.of(ROL_ADMIN, ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR);

    public static final String TIPO_MOVIMIENTO_ENTRADA = "ENTRADA";
    public static final String TIPO_MOVIMIENTO_SALIDA = "SALIDA";

    public static final String ERROR_CARRITO_OBLIGATORIO = "El carrito no puede ser nulo";
    public static final String ERROR_CARRITO_USUARIO_OBLIGATORIO = "El carrito debe estar asociado a un usuario valido";
    public static final String ERROR_CARRITO_PRODUCTO_OBLIGATORIO = "El carrito debe estar asociado a un producto valido";
    public static final String ERROR_CARRITO_CANTIDAD_POSITIVA = "La cantidad del carrito debe ser mayor a cero";
    public static final String ERROR_CARRITO_STOCK_OBLIGATORIO = "El producto del carrito debe tener stock informado";
    public static final String ERROR_CARRITO_STOCK_INSUFICIENTE = "No existe stock suficiente para agregar el producto al carrito";

    public static final String ERROR_VENTA_USUARIO_INVALIDO = "La venta debe estar vinculada a un usuario valido";
    public static final String ERROR_VENTA_USUARIO_INEXISTENTE = "El usuario asociado a la venta no existe";
    public static final String ERROR_VENTA_USUARIO_INCOMPLETO = "El usuario debe tener nombre, apellido, email y direccion";
    public static final String ERROR_VENTA_ROL_NO_AUTORIZADO = "El usuario no tiene un rol autorizado para registrar ventas";
    public static final String ERROR_VENTA_STOCK_INSUFICIENTE = "No existe stock suficiente para registrar la venta";
}