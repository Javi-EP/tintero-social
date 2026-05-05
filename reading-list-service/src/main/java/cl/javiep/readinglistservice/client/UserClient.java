package cl.javiep.readinglistservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${services.user-service.url}") String userServiceUrl) {
        this.restClient = RestClient.create(userServiceUrl);
    }

    // Hace login al arrancar para obtener un token interno
    private String fetchToken(String email, String password) {
        try {
            var response = restClient.post()
                    .uri("/api/users/login")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(Map.of("email", email, "password", password))
                    .retrieve()
                    .toEntity(Map.class)
                    .getBody();
            return (String) response.get("token");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean userExists(Long userId) {
        try {
            restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}