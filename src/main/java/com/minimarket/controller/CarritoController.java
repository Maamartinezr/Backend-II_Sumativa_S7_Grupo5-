package com.minimarket.controller;

import com.minimarket.assembler.CarritoModelAssembler;
import com.minimarket.dto.CarritoDTO;
import com.minimarket.dto.ErrorResponseDTO;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minimarket.util.OpenApiExamples.CARRITO_PAGE;
import static com.minimarket.util.OpenApiExamples.CARRITO_REQUEST;
import static com.minimarket.util.OpenApiExamples.CARRITO_RESPONSE;
import static com.minimarket.util.OpenApiExamples.ERROR_400;
import static com.minimarket.util.OpenApiExamples.ERROR_401;
import static com.minimarket.util.OpenApiExamples.ERROR_403;
import static com.minimarket.util.OpenApiExamples.ERROR_404_CARRITO;
import static com.minimarket.util.OpenApiExamples.ERROR_409;
import static com.minimarket.util.OpenApiExamples.ERROR_500;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Operaciones para administrar el carrito de compras")
@SecurityRequirement(name = "bearerAuth")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoModelAssembler carritoModelAssembler;

    @Operation(
            summary = "Listar carrito",
            description = "Obtiene un listado paginado de items registrados en el carrito. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado del carrito",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CollectionModel.class),
                            examples = @ExampleObject(value = CARRITO_PAGE))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_400))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CarritoDTO>>> listarCarrito(@ParameterObject Pageable pageable) {
        Page<CarritoDTO> carritos = carritoService.findAll(pageable).map(EntityDtoMapper::toCarritoDto);
        CollectionModel<EntityModel<CarritoDTO>> collectionModel = CollectionModel.of(
                carritos.map(carritoModelAssembler::toModel).toList(),
                linkTo(methodOn(CarritoController.class).listarCarrito(pageable)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(
            summary = "Obtener item del carrito por id",
            description = "Recupera un item del carrito por su identificador. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntityModel.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_CARRITO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CarritoDTO>> obtenerCarritoPorId(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del item del carrito", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(carritoModelAssembler.toModel(EntityDtoMapper.toCarritoDto(obtenerCarrito(id))));
    }

    @Operation(
            summary = "Agregar producto al carrito",
            description = "Registra un nuevo item de carrito. Requiere autenticacion JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item del carrito creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoDTO.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_400))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_CARRITO))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del item de carrito a registrar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarritoDTO.class),
                    examples = @ExampleObject(value = CARRITO_REQUEST)))
    @PostMapping
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito(@Valid @RequestBody CarritoDTO carritoDto) {
        Usuario usuario = obtenerUsuario(carritoDto.getUsuarioId());
        Producto producto = obtenerProducto(carritoDto.getProductoId());
        Carrito guardado = carritoService.save(EntityDtoMapper.toCarritoEntity(carritoDto, usuario, producto));
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityDtoMapper.toCarritoDto(guardado));
    }

    @Operation(
            summary = "Actualizar item del carrito",
            description = "Actualiza un item existente del carrito. Requiere autenticacion JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarritoDTO.class),
                            examples = @ExampleObject(value = CARRITO_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_400))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Item, usuario o producto no encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_CARRITO))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos actualizados del item de carrito",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarritoDTO.class),
                    examples = @ExampleObject(value = CARRITO_REQUEST)))
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
            description = "Elimina un item existente del carrito. Requiere autenticacion JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item del carrito eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_CARRITO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
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
