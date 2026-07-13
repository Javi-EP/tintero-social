package cl.javiep.socialservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class UserClient {
    private final RestClient restClient;

    public UserClient(@Value("${services.user-service.url}") String url) {
        this.restClient = RestClient.create(url);
    }

    public boolean userExists(Long userId){
        try{
            restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity();
                return true;
        } catch (Exception e){
            log.warn("Error al verificar usuario {} en user-service: {}", userId, e.getMessage());
            return false;
        }
    }
}
