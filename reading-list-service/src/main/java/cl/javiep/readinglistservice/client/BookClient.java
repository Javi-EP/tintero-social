package cl.javiep.readinglistservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class BookClient {

    private final RestClient restClient;

    public BookClient(@Value("${services.book-service.url}") String bookServiceUrl) {
        // RestClient.create() no necesita @Bean ni configuración extra
        this.restClient = RestClient.create(bookServiceUrl);
    }

    public boolean bookExists(Long bookId) {
        try {
            restClient.get()
                    .uri("/api/books/{id}", bookId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            log.warn("Error al verificar libro {} en book-service: {}", bookId, e.getMessage());
            return false;
        }
    }
}