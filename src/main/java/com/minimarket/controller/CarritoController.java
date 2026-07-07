package com.minimarket.controller;

import com.minimarket.dto.CarritoDTO;
import com.minimarket.dto.ErrorResponseDTO;
import com.minimarket.dto.PageResponse;
import com.minimarket.dto.page.CarritoPageResponse;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.exception.ResourceNotFoundException;
import com.minimarket.mapper.EntityDtoMapper;
import com.minimarket.service.CarritoService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Operaciones para administrar el carrito de compras")
@SecurityRequirement(name = "bearerAuth")
public class CarritoController {

    private static final String CARRITO_REQUEST_EXAMPLE = """
            {
              "usuarioId": 1,
              "productoId": 1,
              "cantidad": 2
            }
            """;
    private static final String CARRITO_RESPONSE_EXAMPLE = """
            {
              "id": 1,
              "usuarioId": 1,
              "productoId": 1,
              "cantidad": 2
            }
            """;
    private static final String CARRITO_PAGE_EXAMPLE = """
            {
              "content": [
                {
                  "id": 1,
                  "usuarioId": 1,
                  "productoId": 1,
                  "cantidad": 2
                }
              ],
              "number": 0,
              "size": 20,
              "totalElements": 1,
              "totalPages": 1,
              "first": true,
              "last": true,
              "empty": false
            }
            """;
    private static final String ERROR_EXAMPLE = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 404,
              "error": "Not Found",
              "message": "Carrito no encontrado con id: 99",
              "path": "/api/carrito/99"
            }
            """;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Listar carrito",
            description = "Obtiene un listado paginado de items registrados en el carrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado del carrito",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoPageResponse.class),
                            examples = @ExampleObject(value = CARRITO_PAGE_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Parametros de paginacion invalidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<CarritoDTO>> listarCarrito(@ParameterObject Pageable pageable) {
        Page<CarritoDTO> carritos = carritoService.findAll(pageable).map(EntityDtoMapper::toCarritoDto);
        return ResponseEntity.ok(PageResponse.from(carritos));
    }

    @Operation(
            summary = "Obtener item del carrito por id",
            description = "Recupera un item del carrito a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoDTO.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_EXAMPLE)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> obtenerCarritoPorId(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del item del carrito", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(EntityDtoMapper.toCarritoDto(obtenerCarrito(id)));
    }

    @Operation(
            summary = "Agregar producto al carrito",
            description = "Registra un nuevo item de carrito vinculando usuario, producto y cantidad solicitada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item del carrito creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoDTO.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Datos del carrito invalidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del item de carrito a registrar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarritoDTO.class),
                    examples = @ExampleObject(value = CARRITO_REQUEST_EXAMPLE)))
    @PostMapping
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito(@Valid @RequestBody CarritoDTO carritoDto) {
        Usuario usuario = obtenerUsuario(carritoDto.getUsuarioId());
        Producto producto = obtenerProducto(carritoDto.getProductoId());
        Carrito guardado = carritoService.save(EntityDtoMapper.toCarritoEntity(carritoDto, usuario, producto));
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityDtoMapper.toCarritoDto(guardado));
    }

    @Operation(
            summary = "Actualizar item del carrito",
            description = "Actualiza un item existente del carrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoDTO.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Datos del carrito invalidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item, usuario o producto no encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos actualizados del item de carrito",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarritoDTO.class),
                    examples = @ExampleObject(value = CARRITO_REQUEST_EXAMPLE)))
    @PutMapping("/{id}")
    public ResponseEntity<CarritoDTO> actualizarCarrito(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del item del carrito", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CarritoDTO carritoDto) {
        obtenerCarrito(id);
        Usuario usuario = obtenerUsuario(carritoDto.getUsuarioId());
        Producto producto = obtenerProducto(carritoDto.getProductoId());
        Carrito carrito = EntityDtoMapper.toCarritoEntity(carritoDto, usuario, producto);
        carrito.setId(id);
        return ResponseEntity.ok(EntityDtoMapper.toCarritoDto(carritoService.save(carrito)));
    }

    @Operation(
            summary = "Eliminar item del carrito",
            description = "Elimina un item existente del carrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item del carrito eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_EXAMPLE)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del item del carrito", example = "1")
            @PathVariable Long id) {
        obtenerCarrito(id);
        carritoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Carrito obtenerCarrito(Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito == null) {
            throw new ResourceNotFoundException("Carrito no encontrado con id: " + id);
        }
        return carrito;
    }

    private Usuario obtenerUsuario(Long id) {
        return usuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private Producto obtenerProducto(Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        return producto;
    }
}
