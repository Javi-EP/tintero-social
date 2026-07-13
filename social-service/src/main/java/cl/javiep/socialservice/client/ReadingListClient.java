package cl.javiep.socialservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ReadingListClient {

    private final RestClient restClient;

    public ReadingListClient(@Value("${services.reading-list-service.url}") String url) {
        this.restClient = RestClient.create(url);
    }

    // Obtiene las listas de un usuario para el feed
    public List<Map<String, Object>> getListsByUser(Long userId) {
        try {
            return restClient.get()
                    .uri("/api/lists/user/{userId}", userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.warn("Error al obtener listas del usuario {} desde reading-list-service: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}