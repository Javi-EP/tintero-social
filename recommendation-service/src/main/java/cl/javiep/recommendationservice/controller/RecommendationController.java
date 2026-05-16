package cl.javiep.recommendationservice.controller;

import cl.javiep.recommendationservice.dto.*;
import cl.javiep.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // GET /api/recommendations/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendations(
            @PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }

    // POST /api/recommendations/refresh/{userId}
    @PostMapping("/refresh/{userId}")
    public ResponseEntity<Void> refresh(@PathVariable Long userId) {
        recommendationService.refreshRecommendations(userId);
        return ResponseEntity.ok().build();
    }

    // POST /api/recommendations/{userId}/dismiss/{bookId}
    @PostMapping("/{userId}/dismiss/{bookId}")
    public ResponseEntity<Void> dismiss(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        recommendationService.dismissRecommendation(userId, bookId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/recommendations/trending
    @GetMapping("/trending")
    public ResponseEntity<List<RecommendationResponseDTO>> getTrending() {
        return ResponseEntity.ok(recommendationService.getTrending());
    }

    // POST /api/recommendations/preferences
    @PostMapping("/preferences")
    public ResponseEntity<Void> addGenrePreference(
            @RequestBody GenrePreferenceDTO dto) {
        recommendationService.addGenrePreference(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/recommendations/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("recommendation-service OK");
    }
}
