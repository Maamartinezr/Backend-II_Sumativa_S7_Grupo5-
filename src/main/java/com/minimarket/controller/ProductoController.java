package com.minimarket.controller;

import com.minimarket.dto.ErrorResponseDTO;
import com.minimarket.dto.PageResponse;
import com.minimarket.dto.ProductoDTO;
import com.minimarket.dto.page.ProductoPageResponse;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.exception.ResourceNotFoundException;
import com.minimarket.mapper.EntityDtoMapper;
import com.minimarket.service.CategoriaService;
import com.minimarket.service.ProductoService;
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

import static com.minimarket.util.OpenApiExamples.ERROR_400;
import static com.minimarket.util.OpenApiExamples.ERROR_401;
import static com.minimarket.util.OpenApiExamples.ERROR_403;
import static com.minimarket.util.OpenApiExamples.ERROR_404_PRODUCTO;
import static com.minimarket.util.OpenApiExamples.ERROR_409;
import static com.minimarket.util.OpenApiExamples.ERROR_500;
import static com.minimarket.util.OpenApiExamples.PRODUCTO_PAGE;
import static com.minimarket.util.OpenApiExamples.PRODUCTO_REQUEST;
import static com.minimarket.util.OpenApiExamples.PRODUCTO_RESPONSE;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones para consultar, crear, actualizar y eliminar productos del minimarket")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Operation(
            summary = "Listar productos",
            description = "Obtiene una lista paginada de productos. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado de productos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoPageResponse.class),
                            examples = @ExampleObject(value = PRODUCTO_PAGE))),
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
    public ResponseEntity<PageResponse<ProductoDTO>> listarProductos(@ParameterObject Pageable pageable) {
        Page<ProductoDTO> productos = productoService.findAll(pageable).map(EntityDtoMapper::toProductoDto);
        return ResponseEntity.ok(PageResponse.from(productos));
    }

    @Operation(
            summary = "Obtener producto por id",
            description = "Recupera el detalle de un producto por su identificador. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoDTO.class),
                            examples = @ExampleObject(value = PRODUCTO_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_PRODUCTO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del producto", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(EntityDtoMapper.toProductoDto(obtenerProducto(id)));
    }

    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoDTO.class),
                            examples = @ExampleObject(value = PRODUCTO_RESPONSE))),
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
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_PRODUCTO))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del producto a crear",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProductoDTO.class),
                    examples = @ExampleObject(value = PRODUCTO_REQUEST)))
    @PostMapping
    public ResponseEntity<ProductoDTO> guardarProducto(@Valid @RequestBody ProductoDTO productoDto) {
        Categoria categoria = obtenerCategoria(productoDto.getCategoriaId());
        Producto guardado = productoService.save(EntityDtoMapper.toProductoEntity(productoDto, categoria));
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityDtoMapper.toProductoDto(guardado));
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza un producto existente. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoDTO.class),
                            examples = @ExampleObject(value = PRODUCTO_RESPONSE))),
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
            @ApiResponse(responseCode = "404", description = "Producto o categoria no encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_PRODUCTO))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del producto a actualizar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProductoDTO.class),
                    examples = @ExampleObject(value = PRODUCTO_REQUEST)))
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del producto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDto) {
        obtenerProducto(id);
        Categoria categoria = obtenerCategoria(productoDto.getCategoriaId());
        Producto producto = EntityDtoMapper.toProductoEntity(productoDto, categoria);
        producto.setId(id);
        return ResponseEntity.ok(EntityDtoMapper.toProductoDto(productoService.save(producto)));
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto existente. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_PRODUCTO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del producto", example = "1")
            @PathVariable Long id) {
        obtenerProducto(id);
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Producto obtenerProducto(Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        return producto;
    }

    private Categoria obtenerCategoria(Long categoriaId) {
        Categoria categoria = categoriaService.findById(categoriaId);
        if (categoria == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + categoriaId);
        }
        return categoria;
    }
}
