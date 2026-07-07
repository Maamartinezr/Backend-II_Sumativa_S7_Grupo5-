package com.minimarket;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void autenticarUsuarioExistenteRetornaCredencialesYPermisos() {
        Usuario usuario = crearUsuario("cajero", "CAJERO");
        when(usuarioRepository.findByUsername("cajero")).thenReturn(Optional.of(usuario));

        UserDetails resultado = customUserDetailsService.loadUserByUsername("cajero");

        assertEquals("cajero", resultado.getUsername());
        assertEquals("clave-encriptada", resultado.getPassword());
        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("CAJERO")));
        verify(usuarioRepository).findByUsername("cajero");
    }

    @Test
    void autenticarUsuarioInexistenteLanzaUsernameNotFoundException() {
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("desconocido"));
        verify(usuarioRepository).findByUsername("desconocido");
    }

    private Usuario crearUsuario(String username, String rol) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername(username);
        usuario.setNombre("Carlos");
        usuario.setApellido("Perez");
        usuario.setEmail("carlos.perez@minimarket.cl");
        usuario.setDireccion("Los Leones 456");
        usuario.setPassword("clave-encriptada");
        usuario.setRoles(Set.of(new Rol(rol)));
        return usuario;
    }
}
