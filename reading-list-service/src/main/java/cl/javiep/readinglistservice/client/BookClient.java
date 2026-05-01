package cl.javiep.readinglistservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
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
            return false;
        }
    }
}