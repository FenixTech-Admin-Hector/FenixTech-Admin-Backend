package com.proyecto.fenixtech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FenixTech API")
                        .version("1.0")
                        .description(
                                "API para la gestión de la plataforma FenixTech, incluyendo categorías, subcategorías, productos, pedidos y usuarios.")
                        .contact(new Contact()
                                .name("FenixTech Support")
                                .email("support@fenixtech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"));
    }

}
