package com.minimarket;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_CANTIDAD_POSITIVA;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_PRODUCTO_OBLIGATORIO;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_STOCK_INSUFICIENTE;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_STOCK_OBLIGATORIO;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_USUARIO_OBLIGATORIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    @Test
    void agregarProductoConStockSuficienteGuardaCarrito() {
        Carrito carrito = crearCarrito(1L, 5);
        Producto producto = crearProducto(10L, "Arroz", 1200.0, 10);
        carrito.setProducto(producto);

        when(carritoRepository.save(carrito)).thenReturn(carrito);

        Carrito resultado = carritoService.save(carrito);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
        assertEquals(10L, resultado.getProducto().getId());
        assertTrue(resultado.getCantidad() <= resultado.getProducto().getStock());
        verify(carritoRepository).save(carrito);
    }

    @Test
    void agregarProductoSinStockSuficienteLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 15);
        carrito.setProducto(crearProducto(10L, "Leche", 950.0, 10));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_STOCK_INSUFICIENTE, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0})
    void agregarProductoConCantidadNoValidaLanzaExcepcionYNoGuarda(int cantidadInvalida) {
        Carrito carrito = crearCarrito(1L, cantidadInvalida);
        carrito.setProducto(crearProducto(10L, "Producto", 1000.0, 20));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_CANTIDAD_POSITIVA, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void guardarCarritoSinUsuarioLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 2);
        carrito.setUsuario(null);
        carrito.setProducto(crearProducto(10L, "Arroz", 1200.0, 10));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_USUARIO_OBLIGATORIO, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void guardarCarritoConUsuarioSinIdLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 2);
        carrito.getUsuario().setId(null);
        carrito.setProducto(crearProducto(10L, "Arroz", 1200.0, 10));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_USUARIO_OBLIGATORIO, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void guardarCarritoSinProductoLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 2);
        carrito.setProducto(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_PRODUCTO_OBLIGATORIO, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void guardarCarritoConProductoSinIdLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 2);
        carrito.setProducto(crearProducto(null, "Producto sin ID", 500.0, 10));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_PRODUCTO_OBLIGATORIO, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void guardarCarritoConStockNuloLanzaExcepcionYNoGuarda() {
        Carrito carrito = crearCarrito(1L, 2);
        carrito.setProducto(crearProducto(10L, "Producto sin stock", 500.0, null));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carritoService.save(carrito));

        assertEquals(ERROR_CARRITO_STOCK_OBLIGATORIO, exception.getMessage());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void validarUsuarioAsociadoAlCarritoRetornaRelacionCorrecta() {
        Usuario usuario = crearUsuario(1L, "carlos.vendedor");
        Carrito carrito = crearCarrito(1L, 3);
        carrito.setUsuario(usuario);
        carrito.setProducto(crearProducto(10L, "Arroz", 1200.0, 20));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getUsuario().getId());
        assertEquals("carlos.vendedor", resultado.getUsuario().getUsername());
        verify(carritoRepository).findById(1L);
    }

    @Test
    void obtenerCarritoPorIdNoExistenteRetornaNulo() {
        when(carritoRepository.findById(999L)).thenReturn(Optional.empty());

        Carrito resultado = carritoService.findById(999L);

        assertNull(resultado);
        verify(carritoRepository).findById(999L);
    }

    @Test
    void obtenerCarritosPorUsuarioRetornaListaCorrecta() {
        Carrito carrito1 = crearCarrito(1L, 2);
        Carrito carrito2 = crearCarrito(2L, 3);
        List<Carrito> carritos = List.of(carrito1, carrito2);

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(carritos);

        List<Carrito> resultado = carritoService.findByUsuarioId(1L);

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        assertEquals(2L, resultado.get(1).getUsuario().getId());
        verify(carritoRepository).findByUsuarioId(1L);
    }

    @Test
    void limpiarCarritoEliminaPorIdentificador() {
        carritoService.deleteById(1L);

        verify(carritoRepository).deleteById(1L);
    }

    private Carrito crearCarrito(Long usuarioId, int cantidad) {
        Carrito carrito = new Carrito();
        carrito.setId(usuarioId);
        carrito.setUsuario(crearUsuario(usuarioId, "usuario" + usuarioId));
        carrito.setCantidad(cantidad);
        return carrito;
    }

    private Usuario crearUsuario(Long id, String username) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setNombre("Nombre" + id);
        usuario.setApellido("Apellido" + id);
        usuario.setEmail("email" + id + "@minimarket.cl");
        usuario.setDireccion("Direccion " + id);
        usuario.setPassword("ClaveSegura123");
        return usuario;
    }

    private Producto crearProducto(Long id, String nombre, Double precio, Integer stock) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        return producto;
    }
}