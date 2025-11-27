package dev.andervilo.gestao_frotas.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the Fleet Management API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoFrotasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestão de Frotas API")
                        .description("API para gestão de frotas de veículos")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Andervilo")
                                .email("andervilo@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}