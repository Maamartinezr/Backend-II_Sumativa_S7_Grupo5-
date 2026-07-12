package com.minimarket.assembler;

import com.minimarket.controller.CarritoController;
import com.minimarket.dto.CarritoDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<CarritoDTO, EntityModel<CarritoDTO>> {

    @Override
    public EntityModel<CarritoDTO> toModel(CarritoDTO carrito) {
        return EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(carrito.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito(Pageable.unpaged())).withRel("collection"));
    }
}
