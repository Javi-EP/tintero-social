package cl.javiep.reviewservice.controller;

import cl.javiep.reviewservice.dto.*;
import cl.javiep.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // POST /api/reviews
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(dto));
    }

    // GET /api/reviews/book/{bookId}
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    // GET /api/reviews/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    // PUT /api/reviews/{id}?userId=X
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, userId, dto));
    }

    // DELETE /api/reviews/{id}?userId=X
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId) {
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    // POST /api/reviews/{id}/vote
    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> vote(
            @PathVariable Long id,
            @RequestBody VoteRequestDTO dto) {
        reviewService.voteReview(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/reviews/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("review-service OK");
    }
}
