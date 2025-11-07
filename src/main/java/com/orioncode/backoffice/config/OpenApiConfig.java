package com.orioncode.backoffice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OrionCode Backoffice API")
                        .version("1.0.0")
                        .description("API REST para gestión de puestos y colaboradores con arquitectura de vertical slicing")
                        .contact(new Contact()
                                .name("OrionCode Team")
                                .email("support@orioncode.com")));
    }
}
