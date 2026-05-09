package cl.javiep.socialservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
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
            return false;
        }
    }
}
