package com.halyk.onlinestore.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        Info info = new Info()
                .title("Halyk Online Store")
                .description("API Description")
                .contact(
                        new Contact()
                                .email("asulanma@gmail.com")
                                .name("Asulan Maksut")
                )
                .version("1.0.0");

        OpenAPI openAPI = new OpenAPI().info(info);

        openAPI.externalDocs(
                new ExternalDocumentation()
                        .description("Project repository in Github")
                        .url("https://github.com/vvotuskam/online-store")
        );

        return openAPI;
    }
}
