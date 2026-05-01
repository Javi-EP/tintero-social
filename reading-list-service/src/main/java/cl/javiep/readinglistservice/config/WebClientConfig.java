package cl.javiep.readinglistservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // WebClient es el cliente HTTP de Spring para llamar a otros servicios
    // Es como un "Postman" dentro del código
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}