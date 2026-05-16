package cl.javiep.statsservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class BookClient {

    private static final Logger log = LoggerFactory.getLogger(BookClient.class);
    private final RestClient restClient;

    public BookClient(@Value("${services.book-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Map<String, Object> getBookById(Long bookId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = restClient.get()
                    .uri("/api/books/{id}", bookId)
                    .retrieve()
                    .body(Map.class);
            return result;
        } catch (Exception e) {
            log.error("Error fetching book {}: {}", bookId, e.getMessage());
            return null;
        }
    }
}