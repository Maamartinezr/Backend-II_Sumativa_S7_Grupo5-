package com.minimarket.controller;

import com.minimarket.assembler.InventarioModelAssembler;
import com.minimarket.dto.ErrorResponseDTO;
import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import java.util.List;

import static com.minimarket.util.OpenApiExamples.ERROR_400;
import static com.minimarket.util.OpenApiExamples.ERROR_401;
import static com.minimarket.util.OpenApiExamples.ERROR_403;
import static com.minimarket.util.OpenApiExamples.ERROR_404_INVENTARIO;
import static com.minimarket.util.OpenApiExamples.ERROR_409;
import static com.minimarket.util.OpenApiExamples.ERROR_500;
import static com.minimarket.util.OpenApiExamples.INVENTARIO_COLLECTION;
import static com.minimarket.util.OpenApiExamples.INVENTARIO_REQUEST;
import static com.minimarket.util.OpenApiExamples.INVENTARIO_RESPONSE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones para gestionar movimientos de inventario")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private InventarioModelAssembler inventarioModelAssembler;

    @Operation(
            summary = "Listar movimientos de inventario",
            description = "Obtiene todos los movimientos registrados en inventario. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de movimientos de inventario",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CollectionModel.class),
                            examples = @ExampleObject(value = INVENTARIO_COLLECTION))),
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
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {
        List<EntityModel<Inventario>> inventarios = inventarioService.findAll().stream()
                .map(inventarioModelAssembler::toModel)
                .toList();
        return CollectionModel.of(inventarios,
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withSelfRel());
    }

    @Operation(
            summary = "Obtener movimiento por id",
            description = "Recupera un movimiento de inventario por su identificador. Requiere autenticacion JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento de inventario encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntityModel.class),
                            examples = @ExampleObject(value = INVENTARIO_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_INVENTARIO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del movimiento de inventario", example = "1")
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null)
                ? ResponseEntity.ok(inventarioModelAssembler.toModel(inventario))
                : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Registrar movimiento",
            description = "Registra un nuevo movimiento en inventario.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Inventario.class),
                            examples = @ExampleObject(value = INVENTARIO_RESPONSE))),
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
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del movimiento de inventario a registrar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Inventario.class),
                    examples = @ExampleObject(value = INVENTARIO_REQUEST)))
    @PostMapping
    public ResponseEntity<Inventario> registrarMovimiento(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.save(inventario));
    }

    @Operation(
            summary = "Actualizar movimiento",
            description = "Actualiza un movimiento de inventario existente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Inventario.class),
                            examples = @ExampleObject(value = INVENTARIO_RESPONSE))),
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
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_INVENTARIO))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_409))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos del movimiento de inventario a actualizar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Inventario.class),
                    examples = @ExampleObject(value = INVENTARIO_REQUEST)))
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarMovimiento(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del movimiento de inventario", example = "1")
            @PathVariable Long id, @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioService.save(inventario));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar movimiento",
            description = "Elimina un movimiento de inventario existente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_401))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_403))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_404_INVENTARIO))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = ERROR_500)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(in = ParameterIn.PATH, description = "Identificador unico del movimiento de inventario", example = "1")
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
