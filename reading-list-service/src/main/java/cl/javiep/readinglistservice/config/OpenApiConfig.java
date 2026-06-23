package cl.javiep.readinglistservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI readingListsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reading Lists API")
                        .description("API REST para gestion de listas de lectura personalizadas")
                        .version("1.0.0")
                        .license(new License().name("Uso educativo DSY1103")));
    }
}
