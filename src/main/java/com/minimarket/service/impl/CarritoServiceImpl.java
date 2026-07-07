package com.minimarket.service.impl;

import com.minimarket.entity.Carrito;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_CANTIDAD_POSITIVA;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_OBLIGATORIO;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_PRODUCTO_OBLIGATORIO;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_STOCK_INSUFICIENTE;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_STOCK_OBLIGATORIO;
import static com.minimarket.util.MinimarketConstants.ERROR_CARRITO_USUARIO_OBLIGATORIO;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    @Override
    public Page<Carrito> findAll(Pageable pageable) {
        return carritoRepository.findAll(pageable);
    }

    @Override
    public Carrito findById(Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    @Override
    public Carrito save(Carrito carrito) {
        validarCarrito(carrito);
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteById(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public List<Carrito> findByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    private void validarCarrito(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException(ERROR_CARRITO_OBLIGATORIO);
        }

        if (!tieneUsuarioValido(carrito)) {
            throw new IllegalArgumentException(ERROR_CARRITO_USUARIO_OBLIGATORIO);
        }

        if (!tieneProductoValido(carrito)) {
            throw new IllegalArgumentException(ERROR_CARRITO_PRODUCTO_OBLIGATORIO);
        }

        if (!cantidadSolicitadaValida(carrito)) {
            throw new IllegalArgumentException(ERROR_CARRITO_CANTIDAD_POSITIVA);
        }

        if (!stockInformado(carrito)) {
            throw new IllegalArgumentException(ERROR_CARRITO_STOCK_OBLIGATORIO);
        }

        if (!tieneStockSuficiente(carrito)) {
            throw new IllegalArgumentException(ERROR_CARRITO_STOCK_INSUFICIENTE);
        }
    }

    private boolean tieneUsuarioValido(Carrito carrito) {
        return carrito.getUsuario() != null && carrito.getUsuario().getId() != null;
    }

    private boolean tieneProductoValido(Carrito carrito) {
        return carrito.getProducto() != null && carrito.getProducto().getId() != null;
    }

    private boolean cantidadSolicitadaValida(Carrito carrito) {
        return carrito.getCantidad() != null && carrito.getCantidad() > 0;
    }

    private boolean stockInformado(Carrito carrito) {
        return carrito.getProducto().getStock() != null;
    }

    private boolean tieneStockSuficiente(Carrito carrito) {
        return carrito.getProducto().getStock() >= carrito.getCantidad();
    }
}