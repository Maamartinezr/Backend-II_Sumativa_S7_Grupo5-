package com.minimarket;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityJpaIdentityTest {

    @Test
    void productoUsaIdentidadJpaBasadaEnId() {
        Producto producto = crearProducto(1L);
        Producto mismoId = crearProducto(1L);
        Producto otroId = crearProducto(2L);
        Producto sinId = crearProducto(null);

        assertTrue(producto.equals(producto));
        assertEquals(producto, mismoId);
        assertNotEquals(producto, otroId);
        assertNotEquals(producto, null);
        assertNotEquals(producto, new Categoria());
        assertNotEquals(sinId, crearProducto(null));
        assertEquals(Producto.class.hashCode(), producto.hashCode());
        assertEquals("Arroz", producto.getNombre());
        assertEquals(1200.0, producto.getPrecio());
        assertEquals(20, producto.getStock());
    }

    @Test
    void usuarioUsaIdentidadJpaBasadaEnIdYExponeRoles() {
        Usuario usuario = crearUsuario(1L);
        Usuario mismoId = crearUsuario(1L);
        Usuario otroId = crearUsuario(2L);
        Rol rol = new Rol("CAJERO");
        usuario.setRoles(Set.of(rol));

        assertTrue(usuario.equals(usuario));
        assertEquals(usuario, mismoId);
        assertNotEquals(usuario, otroId);
        assertNotEquals(usuario, null);
        assertNotEquals(usuario, new Producto());
        assertNotEquals(crearUsuario(null), crearUsuario(null));
        assertEquals(Usuario.class.hashCode(), usuario.hashCode());
        assertEquals("usuario1", usuario.getUsername());
        assertEquals("Nombre1", usuario.getNombre());
        assertEquals("Apellido1", usuario.getApellido());
        assertEquals("usuario1@minimarket.cl", usuario.getEmail());
        assertEquals("Direccion 1", usuario.getDireccion());
        assertEquals("ClaveSegura123", usuario.getPassword());
        assertTrue(usuario.getRoles().contains(rol));
    }

    @Test
    void rolConstructoresGettersSettersEIdentidadJpa() {
        Usuario usuario = crearUsuario(1L);
        Rol rol = new Rol(1L, "ADMIN", Set.of(usuario));
        Rol mismoId = new Rol(1L, "ADMIN", Set.of(usuario));
        Rol otroId = new Rol(2L, "CLIENTE", Set.of(usuario));
        Rol soloNombre = new Rol("CAJERO");

        assertEquals(rol, mismoId);
        assertNotEquals(rol, otroId);
        assertNotEquals(rol, null);
        assertNotEquals(rol, new Usuario());
        assertNotEquals(new Rol(), new Rol());
        assertEquals(Rol.class.hashCode(), rol.hashCode());
        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertTrue(rol.getUsuarios().contains(usuario));
        assertEquals("CAJERO", soloNombre.getNombre());
    }

    @Test
    void categoriaGettersSettersEIdentidadJpa() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");
        Producto producto = crearProducto(10L);
        categoria.setProductos(List.of(producto));

        Categoria mismoId = new Categoria();
        mismoId.setId(1L);
        Categoria otroId = new Categoria();
        otroId.setId(2L);

        assertEquals(categoria, mismoId);
        assertNotEquals(categoria, otroId);
        assertNotEquals(categoria, null);
        assertNotEquals(categoria, new Rol());
        assertNotEquals(new Categoria(), new Categoria());
        assertEquals(Categoria.class.hashCode(), categoria.hashCode());
        assertEquals("Abarrotes", categoria.getNombre());
        assertSame(producto, categoria.getProductos().get(0));
    }

    @Test
    void carritoGettersSettersEIdentidadJpa() {
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        carrito.setUsuario(crearUsuario(1L));
        carrito.setProducto(crearProducto(1L));
        carrito.setCantidad(3);

        Carrito mismoId = new Carrito();
        mismoId.setId(1L);
        Carrito otroId = new Carrito();
        otroId.setId(2L);

        assertEquals(carrito, mismoId);
        assertNotEquals(carrito, otroId);
        assertNotEquals(carrito, null);
        assertNotEquals(carrito, new Venta());
        assertNotEquals(new Carrito(), new Carrito());
        assertEquals(Carrito.class.hashCode(), carrito.hashCode());
        assertEquals(3, carrito.getCantidad());
        assertEquals(1L, carrito.getUsuario().getId());
        assertEquals(1L, carrito.getProducto().getId());
    }

    @Test
    void inventarioGettersSettersEIdentidadJpa() {
        Inventario inventario = new Inventario();
        Date fecha = new Date();
        inventario.setId(1L);
        inventario.setProducto(crearProducto(1L));
        inventario.setCantidad(8);
        inventario.setTipoMovimiento("ENTRADA");
        inventario.setFechaMovimiento(fecha);

        Inventario mismoId = new Inventario();
        mismoId.setId(1L);
        Inventario otroId = new Inventario();
        otroId.setId(2L);

        assertEquals(inventario, mismoId);
        assertNotEquals(inventario, otroId);
        assertNotEquals(inventario, null);
        assertNotEquals(inventario, new DetalleVenta());
        assertNotEquals(new Inventario(), new Inventario());
        assertEquals(Inventario.class.hashCode(), inventario.hashCode());
        assertEquals(8, inventario.getCantidad());
        assertEquals("ENTRADA", inventario.getTipoMovimiento());
        assertSame(fecha, inventario.getFechaMovimiento());
    }

    @Test
    void ventaGettersSettersEIdentidadJpa() {
        Venta venta = new Venta();
        Date fecha = new Date();
        DetalleVenta detalle = new DetalleVenta();
        venta.setId(1L);
        venta.setUsuario(crearUsuario(1L));
        venta.setFecha(fecha);
        venta.setTotal(2500.0);
        venta.setDetalles(List.of(detalle));

        Venta mismoId = new Venta();
        mismoId.setId(1L);
        Venta otroId = new Venta();
        otroId.setId(2L);

        assertEquals(venta, mismoId);
        assertNotEquals(venta, otroId);
        assertNotEquals(venta, null);
        assertNotEquals(venta, new Carrito());
        assertNotEquals(new Venta(), new Venta());
        assertEquals(Venta.class.hashCode(), venta.hashCode());
        assertEquals(2500.0, venta.getTotal());
        assertSame(fecha, venta.getFecha());
        assertSame(detalle, venta.getDetalles().get(0));
    }

    @Test
    void detalleVentaGettersSettersEIdentidadJpa() {
        DetalleVenta detalle = new DetalleVenta();
        Venta venta = new Venta();
        Producto producto = crearProducto(1L);
        detalle.setId(1L);
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecio(1200.0);

        DetalleVenta mismoId = new DetalleVenta();
        mismoId.setId(1L);
        DetalleVenta otroId = new DetalleVenta();
        otroId.setId(2L);

        assertEquals(detalle, mismoId);
        assertNotEquals(detalle, otroId);
        assertNotEquals(detalle, null);
        assertNotEquals(detalle, new Inventario());
        assertNotEquals(new DetalleVenta(), new DetalleVenta());
        assertEquals(DetalleVenta.class.hashCode(), detalle.hashCode());
        assertSame(venta, detalle.getVenta());
        assertSame(producto, detalle.getProducto());
        assertEquals(2, detalle.getCantidad());
        assertEquals(1200.0, detalle.getPrecio());
    }

    @Test
    void entidadesConIdNuloNoSonIgualesAEntidadesConId() {
        Producto productoSinId = crearProducto(null);
        Producto productoConId = crearProducto(1L);

        assertFalse(productoSinId.equals(productoConId));
        assertNull(productoSinId.getId());
    }

    private Producto crearProducto(Long id) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Arroz");
        producto.setPrecio(1200.0);
        producto.setStock(20);
        return producto;
    }

    private Usuario crearUsuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername("usuario" + id);
        usuario.setNombre("Nombre" + id);
        usuario.setApellido("Apellido" + id);
        usuario.setEmail("usuario" + id + "@minimarket.cl");
        usuario.setDireccion("Direccion " + id);
        usuario.setPassword("ClaveSegura123");
        return usuario;
    }
}