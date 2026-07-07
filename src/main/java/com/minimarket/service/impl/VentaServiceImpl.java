package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.minimarket.util.MinimarketConstants.ERROR_VENTA_ROL_NO_AUTORIZADO;
import static com.minimarket.util.MinimarketConstants.ERROR_VENTA_STOCK_INSUFICIENTE;
import static com.minimarket.util.MinimarketConstants.ERROR_VENTA_USUARIO_INCOMPLETO;
import static com.minimarket.util.MinimarketConstants.ERROR_VENTA_USUARIO_INEXISTENTE;
import static com.minimarket.util.MinimarketConstants.ERROR_VENTA_USUARIO_INVALIDO;
import static com.minimarket.util.MinimarketConstants.ROLES_REGISTRO_VENTA;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Venta registrarVenta(Venta venta) {
        validarVenta(venta);
        venta.setTotal(calcularTotal(venta));
        return ventaRepository.save(venta);
    }

    @Override
    public boolean tieneStockSuficiente(Venta venta) {
        if (venta == null || venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            return false;
        }

        return venta.getDetalles().stream().allMatch(detalle -> {
            Producto producto = obtenerProducto(detalle);
            return producto != null
                    && detalle.getCantidad() != null
                    && detalle.getCantidad() > 0
                    && producto.getStock() != null
                    && producto.getStock() >= detalle.getCantidad();
        });
    }

    @Override
    public double calcularTotal(Venta venta) {
        if (venta == null || venta.getDetalles() == null) {
            return 0.0;
        }

        return venta.getDetalles().stream()
                .mapToDouble(detalle -> {
                    Producto producto = obtenerProducto(detalle);
                    if (producto == null || producto.getPrecio() == null || detalle.getCantidad() == null) {
                        return 0.0;
                    }
                    return producto.getPrecio() * detalle.getCantidad();
                })
                .sum();
    }

    private void validarVenta(Venta venta) {
        if (venta == null || venta.getUsuario() == null || venta.getUsuario().getId() == null) {
            throw new IllegalArgumentException(ERROR_VENTA_USUARIO_INVALIDO);
        }

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException(ERROR_VENTA_USUARIO_INEXISTENTE));

        if (!usuarioTieneDatosCompletos(usuario)) {
            throw new IllegalArgumentException(ERROR_VENTA_USUARIO_INCOMPLETO);
        }

        if (!usuarioTieneRolPermitido(usuario)) {
            throw new IllegalArgumentException(ERROR_VENTA_ROL_NO_AUTORIZADO);
        }

        if (!tieneStockSuficiente(venta)) {
            throw new IllegalArgumentException(ERROR_VENTA_STOCK_INSUFICIENTE);
        }
    }

    private Producto obtenerProducto(DetalleVenta detalle) {
        if (detalle == null || detalle.getProducto() == null) {
            return null;
        }

        Long productoId = detalle.getProducto().getId();
        if (productoId == null) {
            return detalle.getProducto();
        }

        return productoRepository.findById(productoId).orElse(detalle.getProducto());
    }

    private boolean usuarioTieneDatosCompletos(Usuario usuario) {
        return noEstaVacio(usuario.getNombre())
                && noEstaVacio(usuario.getApellido())
                && noEstaVacio(usuario.getEmail())
                && noEstaVacio(usuario.getDireccion());
    }

    private boolean usuarioTieneRolPermitido(Usuario usuario) {
        return usuario.getRoles() != null
                && usuario.getRoles().stream().anyMatch(rol -> ROLES_REGISTRO_VENTA.contains(rol.getNombre()));
    }

    private boolean noEstaVacio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}