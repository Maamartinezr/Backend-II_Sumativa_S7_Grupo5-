package com.minimarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.assembler.CarritoModelAssembler;
import com.minimarket.assembler.InventarioModelAssembler;
import com.minimarket.assembler.ProductoModelAssembler;
import com.minimarket.assembler.UsuarioModelAssembler;
import com.minimarket.controller.CarritoController;
import com.minimarket.controller.CategoriaController;
import com.minimarket.controller.DetalleVentaController;
import com.minimarket.controller.InventarioController;
import com.minimarket.controller.ProductoController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.controller.VentaController;
import com.minimarket.dto.CarritoDTO;
import com.minimarket.dto.ProductoDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.service.CarritoService;
import com.minimarket.service.CategoriaService;
import com.minimarket.service.DetalleVentaService;
import com.minimarket.service.InventarioService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.UsuarioService;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ProductoController.class,
        InventarioController.class,
        VentaController.class,
        UsuarioController.class,
        CarritoController.class,
        CategoriaController.class,
        DetalleVentaController.class
})
@AutoConfigureMockMvc(addFilters = false)
class ControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CarritoService carritoService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ProductoModelAssembler productoModelAssembler;

    @MockBean
    private CarritoModelAssembler carritoModelAssembler;

    @MockBean
    private InventarioModelAssembler inventarioModelAssembler;

    @MockBean
    private UsuarioModelAssembler usuarioModelAssembler;

    @BeforeEach
    void configurarAssemblers() {
        when(productoModelAssembler.toModel(any(ProductoDTO.class)))
                .thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0),
                        Link.of("http://localhost/api/productos/1").withSelfRel(),
                        Link.of("http://localhost/api/productos").withRel("collection")));
        when(carritoModelAssembler.toModel(any(CarritoDTO.class)))
                .thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0),
                        Link.of("http://localhost/api/carrito/1").withSelfRel(),
                        Link.of("http://localhost/api/carrito").withRel("collection")));
        when(inventarioModelAssembler.toModel(any(Inventario.class)))
                .thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0),
                        Link.of("http://localhost/api/inventario/1").withSelfRel(),
                        Link.of("http://localhost/api/inventario").withRel("collection")));
        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenAnswer(invocation -> EntityModel.of(invocation.getArgument(0),
                        Link.of("http://localhost/api/usuarios/1").withSelfRel(),
                        Link.of("http://localhost/api/usuarios").withRel("collection")));
    }

    @Test
    void listarProductosRetornaProductosDelServicio() throws Exception {
        when(productoService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(crearProducto(1L), crearProducto(2L)), PageRequest.of(0, 2), 2));

        mockMvc.perform(get("/api/productos?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].nombre").value("Arroz 1"))
                .andExpect(jsonPath("$._embedded.*[1].nombre").value("Arroz 2"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(productoService).findAll(any(Pageable.class));
    }

    @Test
    void obtenerProductoExistenteRetornaOk() throws Exception {
        when(productoService.findById(1L)).thenReturn(crearProducto(1L));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.categoriaId").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(productoService).findById(1L);
    }

    @Test
    void obtenerProductoNoExistenteRetornaNotFound() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarProductoRetornaProductoPersistido() throws Exception {
        Producto producto = crearProducto(1L);
        when(categoriaService.findById(1L)).thenReturn(crearCategoria(1L));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearProductoDto(null))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Arroz 1"));

        verify(productoService).save(any(Producto.class));
    }

    @Test
    void actualizarProductoExistenteRetornaOkYFijaId() throws Exception {
        Producto producto = crearProducto(1L);
        when(productoService.findById(1L)).thenReturn(producto);
        when(categoriaService.findById(1L)).thenReturn(crearCategoria(1L));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearProductoDto(1L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(productoService).save(any(Producto.class));
    }

    @Test
    void actualizarProductoNoExistenteRetornaNotFoundYNoGuarda() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearProductoDto(99L))))
                .andExpect(status().isNotFound());

        verify(productoService, never()).save(any(Producto.class));
    }

    @Test
    void guardarProductoInvalidoRetornaBadRequestConErroresDeValidacion() throws Exception {
        ProductoDTO productoDto = crearProductoDto(null);
        productoDto.setNombre(" ");
        productoDto.setPrecio(-1.0);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.nombre").exists())
                .andExpect(jsonPath("$.validationErrors.precio").exists());
    }

    @Test
    void eliminarProductoExistenteRetornaNoContent() throws Exception {
        when(productoService.findById(1L)).thenReturn(crearProducto(1L));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService).deleteById(1L);
    }

    @Test
    void eliminarProductoNoExistenteRetornaNotFoundYNoElimina() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());

        verify(productoService, never()).deleteById(99L);
    }

    @Test
    void inventarioCrudCubreEntradasSalidasYNoEncontrados() throws Exception {
        Inventario inventario = crearInventario(1L, "ENTRADA");
        when(inventarioService.findAll()).thenReturn(List.of(inventario));
        when(inventarioService.findById(1L)).thenReturn(inventario);
        when(inventarioService.findById(99L)).thenReturn(null);
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].tipoMovimiento").value("ENTRADA"))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoMovimiento").value("ENTRADA"))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearInventario(null, "SALIDA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoMovimiento").value("ENTRADA"));
        mockMvc.perform(put("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/inventario/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/inventario/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/inventario/99"))
                .andExpect(status().isNotFound());

        verify(inventarioService).deleteById(1L);
    }

    @Test
    void ventaControllerListaObtieneYRegistraVentas() throws Exception {
        Venta venta = crearVenta(1L);
        when(ventaService.findAll()).thenReturn(List.of(venta));
        when(ventaService.findById(1L)).thenReturn(venta);
        when(ventaService.findById(99L)).thenReturn(null);
        when(ventaService.registrarVenta(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2400.0));
        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2400.0));

        verify(ventaService).registrarVenta(any(Venta.class));
    }

    @Test
    void usuarioControllerCubreAutenticacionLogicaCrud() throws Exception {
        Usuario usuario = crearUsuario(1L);
        when(usuarioService.findAll()).thenReturn(List.of(usuario));
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].username").value("usuario1"))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("usuario1@minimarket.cl"));
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService).deleteById(1L);
    }

    @Test
    void carritoControllerCubreCrudCompleto() throws Exception {
        Carrito carrito = crearCarrito(1L);
        when(carritoService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(carrito), PageRequest.of(0, 1), 1));
        when(carritoService.findById(1L)).thenReturn(carrito);
        when(carritoService.findById(99L)).thenReturn(null);
        when(usuarioService.findById(1L)).thenReturn(Optional.of(crearUsuario(1L)));
        when(productoService.findById(1L)).thenReturn(crearProducto(1L));
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito);

        mockMvc.perform(get("/api/carrito?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].productoId").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/carrito/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearCarritoDto(null))))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/api/carrito/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearCarritoDto(1L))))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/carrito/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearCarritoDto(99L))))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/carrito/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/carrito/99"))
                .andExpect(status().isNotFound());

        verify(carritoService).deleteById(1L);
    }

    @Test
    void guardarCarritoInvalidoRetornaBadRequestConErroresDeValidacion() throws Exception {
        CarritoDTO carritoDto = crearCarritoDto(null);
        carritoDto.setCantidad(0);
        carritoDto.setProductoId(null);

        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.cantidad").exists())
                .andExpect(jsonPath("$.validationErrors.productoId").exists());
    }

    @Test
    void categoriaControllerCubreCrudCompleto() throws Exception {
        Categoria categoria = crearCategoria(1L);
        when(categoriaService.findAll()).thenReturn(List.of(categoria));
        when(categoriaService.findById(1L)).thenReturn(categoria);
        when(categoriaService.findById(99L)).thenReturn(null);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Abarrotes"));
        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());

        verify(categoriaService).deleteById(1L);
    }

    @Test
    void detalleVentaControllerCubreCrudCompleto() throws Exception {
        DetalleVenta detalle = crearDetalleVenta(1L);
        when(detalleVentaService.findAll()).thenReturn(List.of(detalle));
        when(detalleVentaService.findById(1L)).thenReturn(detalle);
        when(detalleVentaService.findById(99L)).thenReturn(null);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalle);

        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/detalle-ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2));
        mockMvc.perform(get("/api/detalle-ventas/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/detalle-ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalle)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/detalle-ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalle)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/detalle-ventas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalle)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/detalle-ventas/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/detalle-ventas/99"))
                .andExpect(status().isNotFound());

        verify(detalleVentaService).deleteById(1L);
    }

    private Producto crearProducto(Long id) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Arroz " + id);
        producto.setPrecio(1200.0);
        producto.setStock(20);
        producto.setCategoria(crearCategoria(1L));
        return producto;
    }

    private Inventario crearInventario(Long id, String tipoMovimiento) {
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setProducto(crearProducto(1L));
        inventario.setCantidad(5);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventario.setFechaMovimiento(new Date());
        return inventario;
    }

    private Venta crearVenta(Long id) {
        Venta venta = new Venta();
        venta.setId(id);
        venta.setUsuario(crearUsuario(1L));
        venta.setFecha(new Date());
        venta.setTotal(2400.0);
        return venta;
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

    private Carrito crearCarrito(Long id) {
        Carrito carrito = new Carrito();
        carrito.setId(id);
        carrito.setUsuario(crearUsuario(1L));
        carrito.setProducto(crearProducto(1L));
        carrito.setCantidad(2);
        return carrito;
    }

    private ProductoDTO crearProductoDto(Long id) {
        ProductoDTO productoDto = new ProductoDTO();
        productoDto.setId(id);
        productoDto.setNombre("Arroz 1");
        productoDto.setPrecio(1200.0);
        productoDto.setStock(20);
        productoDto.setCategoriaId(1L);
        return productoDto;
    }

    private CarritoDTO crearCarritoDto(Long id) {
        CarritoDTO carritoDto = new CarritoDTO();
        carritoDto.setId(id);
        carritoDto.setUsuarioId(1L);
        carritoDto.setProductoId(1L);
        carritoDto.setCantidad(2);
        return carritoDto;
    }

    private Categoria crearCategoria(Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre("Abarrotes");
        return categoria;
    }

    private DetalleVenta crearDetalleVenta(Long id) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(id);
        detalle.setProducto(crearProducto(1L));
        detalle.setCantidad(2);
        detalle.setPrecio(1200.0);
        return detalle;
    }
}