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
public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);
    private final RestClient restClient;

    public UserClient(@Value("${services.user-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public List<Map<String, Object>> getAllUsers() {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> result = restClient.get()
                    .uri("/api/users")
                    .retrieve()
                    .body(List.class);
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching all users: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Map<String, Object> getUserById(Long userId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .body(Map.class);
            return result;
        } catch (Exception e) {
            log.error("Error fetching user {}: {}", userId, e.getMessage());
            return null;
        }
    }
}