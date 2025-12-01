package com.LucidCart.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("LucidCart API")
                    .description(
                        "Documentação da API do LucidCart.\n" +
                                "Use o botão Authorize para inserir o JWT e acessar os endpoints protegidos."
                    )
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("LucidCart Dev Team")
                            .email("devteam@lucidcart.com")
                            .url("https://github.com/FlavioMatias/LucidCart-gateway")
                    )
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token obtido via /api/v1/auth/signin")
                )
            )
    }
}
