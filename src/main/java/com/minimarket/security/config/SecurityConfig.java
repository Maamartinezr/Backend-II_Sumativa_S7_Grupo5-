package com.minimarket.security.config;

import com.minimarket.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.minimarket.util.MinimarketConstants.ROL_ADMIN;
import static com.minimarket.util.MinimarketConstants.ROL_CAJERO;
import static com.minimarket.util.MinimarketConstants.ROL_EMPLEADO;
import static com.minimarket.util.MinimarketConstants.ROL_VENDEDOR;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority(ROL_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority(ROL_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority(ROL_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/inventario/**").hasAnyAuthority(ROL_ADMIN, ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR)
                        .requestMatchers(HttpMethod.PUT, "/api/inventario/**").hasAnyAuthority(ROL_ADMIN, ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR)
                        .requestMatchers(HttpMethod.DELETE, "/api/inventario/**").hasAnyAuthority(ROL_ADMIN, ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR)
                        .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyAuthority(ROL_CAJERO, ROL_EMPLEADO, ROL_VENDEDOR)
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ConfiguraciÃ³n de encriptaciÃ³n de contraseÃ±as
    }
}
