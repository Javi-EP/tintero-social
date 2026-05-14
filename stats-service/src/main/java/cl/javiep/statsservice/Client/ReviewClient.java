package cl.javiep.statsservice.Client;

import cl.javiep.statsservice.dto.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "review-service",
        url = "http://localhost:8083"
)
public interface ReviewClient {
    @GetMapping("/api/reviews/user/{userId}")
    List<ReviewDTO> getReviewsByUser(
            @PathVariable Long userId);
}
