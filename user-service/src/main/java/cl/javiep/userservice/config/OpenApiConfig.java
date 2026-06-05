package cl.javiep.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usersOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .description("API REST para gestion de usuarios")
                        .version("1.0.0")
                        .license(new License().name("Uso educativo DSY1103")));
    }
}