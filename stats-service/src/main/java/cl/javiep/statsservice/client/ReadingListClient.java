package cl.javiep.statsservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ReadingListClient {

    private static final Logger log = LoggerFactory.getLogger(ReadingListClient.class);
    private final RestClient restClient;

    public ReadingListClient(@Value("${services.reading-list-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public List<Map<String, Object>> getListsByUser(Long userId) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> result = restClient.get()
                    .uri("/api/lists/user/{userId}", userId)
                    .retrieve()
                    .body(List.class);
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching reading lists for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}