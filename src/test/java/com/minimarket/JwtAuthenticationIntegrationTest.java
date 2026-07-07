package com.minimarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.dto.ProductoDTO;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static com.minimarket.util.MinimarketConstants.ROL_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void loginConCredencialesValidasRetornaTokenJwt() throws Exception {
        String username = "jwt-login-admin";
        String password = "Admin123*";
        crearUsuario(username, password, ROL_ADMIN);

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").isNumber());
    }

    @Test
    void endpointProtegidoSinTokenRetornaUnauthorized() throws Exception {
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearProductoDto(1L))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void endpointProtegidoConJwtValidoYRolPermitidoAutentica() throws Exception {
        String username = "jwt-admin-update";
        crearUsuario(username, "Admin123*", ROL_ADMIN);

        Categoria categoria = new Categoria();
        categoria.setNombre("Abarrotes JWT");
        categoria = categoriaRepository.save(categoria);

        Producto producto = new Producto();
        producto.setNombre("Arroz JWT");
        producto.setPrecio(1200.0);
        producto.setStock(10);
        producto.setCategoria(categoria);
        producto = productoRepository.save(producto);

        String token = jwtUtil.generateToken(username);

        MvcResult result = mockMvc.perform(put("/api/productos/{id}", producto.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearProductoDto(categoria.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void openApiIncluyeAuthLoginYSecuritySchemeBearer() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/auth/login']").exists())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat").value("JWT"));
    }

    private Usuario crearUsuario(String username, String password, String rolNombre) {
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseGet(() -> rolRepository.save(new Rol(rolNombre)));

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNombre("Nombre");
        usuario.setApellido("Apellido");
        usuario.setEmail(username + "@mail.com");
        usuario.setDireccion("Calle 123");
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRoles(Set.of(rol));
        return usuarioRepository.save(usuario);
    }

    private ProductoDTO crearProductoDto(Long categoriaId) {
        ProductoDTO productoDto = new ProductoDTO();
        productoDto.setNombre("Arroz Actualizado");
        productoDto.setPrecio(1500.0);
        productoDto.setStock(20);
        productoDto.setCategoriaId(categoriaId);
        return productoDto;
    }
}
