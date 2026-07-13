package cl.javiep.socialservice.client;

import cl.javiep.socialservice.dto.ReviewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ReviewClient {
    private final RestClient restClient;

    public ReviewClient(@Value("${services.review-service.url}") String url){
        this.restClient = RestClient.create(url);
    }

    public List<ReviewDTO> getReviewsByUser(Long userId){
        try{
            return restClient.get()
                    .uri("/api/reviews/user/{userId}")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ReviewDTO>>() {});
                    }catch (Exception e){
                        log.warn("Error al obtener resenas del usuario {} desde review-service: {}", userId, e.getMessage());
                        return Collections.emptyList();
        }
    }
}
