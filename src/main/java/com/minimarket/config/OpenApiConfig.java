package com.minimarket.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI minimarketOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Minimarket API")
                        .version("v1")
                        .description("Contrato OpenAPI para la gestion de productos, carritos y recursos principales del minimarket.")
                        .contact(new Contact()
                                .name("Maamartinezr")
                                .url("https://github.com/Maamartinezr/Backend-II_Sumativa_S7_Grupo5-"))
                        .license(new License()
                                .name("Sin licencia definida")
                                .url("https://github.com/Maamartinezr/Backend-II_Sumativa_S7_Grupo5-")))
                .addServersItem(new Server()
                        .url("http://localhost:9090")
                        .description("Entorno local"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME));
    }
}
