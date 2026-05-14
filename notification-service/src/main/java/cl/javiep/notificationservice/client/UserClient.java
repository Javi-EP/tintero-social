package cl.javiep.notificationservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {
    private final RestClient restClient;

    public UserClient(@Value("${services.user-service.url}") String userServiceUrl) {
        this.restClient = RestClient.create(userServiceUrl);
    }

    public UserResponse getUserById(Long id) {
        return restClient.get()
                .uri("/api/users/{id}", id)
                .retrieve()
                .body(UserResponse.class);
    }

    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String bio;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
    }
}