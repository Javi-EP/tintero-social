package cl.javiep.reviewservice.service;

import cl.javiep.reviewservice.dto.*;
import cl.javiep.reviewservice.entity.*;
import cl.javiep.reviewservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final VoteRepository voteRepository;

    // --- Crear reseña ---
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {

        // Validar que el rating esté entre 1 y 5
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rating must be between 1 and 5");
        }

        // Un usuario solo puede tener una reseña por libro
        if (reviewRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already have a review for this book");
        }

        Review review = Review.builder()
                .userId(dto.getUserId())
                .bookId(dto.getBookId())
                .rating(dto.getRating())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        return toDTO(reviewRepository.save(review));
    }

    // --- Listar reseñas por libro ---
    public List<ReviewResponseDTO> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Listar reseñas por usuario ---
    public List<ReviewResponseDTO> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Editar reseña ---
    public ReviewResponseDTO updateReview(Long id, Long userId, ReviewRequestDTO dto) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Review not found"));

        // Solo el autor puede editar su reseña
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only edit your own reviews");
        }

        if (dto.getRating() != null) {
            if (dto.getRating() < 1 || dto.getRating() > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rating must be between 1 and 5");
            }
            review.setRating(dto.getRating());
        }

        if (dto.getTitle() != null) review.setTitle(dto.getTitle());
        if (dto.getContent() != null) review.setContent(dto.getContent());

        return toDTO(reviewRepository.save(review));
    }

    // --- Eliminar reseña ---
    public void deleteReview(Long id, Long userId) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Review not found"));

        // Solo el autor puede eliminar su reseña
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    // --- Votar reseña como útil ---
    public void voteReview(Long reviewId, VoteRequestDTO dto) {

        // Verificar que la reseña exista
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Review not found");
        }

        // Evitar voto duplicado
        if (voteRepository.existsByUserIdAndReviewId(dto.getUserId(), reviewId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already voted this review");
        }

        Vote vote = Vote.builder()
                .userId(dto.getUserId())
                .reviewId(reviewId)
                .type("USEFUL")
                .build();

        voteRepository.save(vote);
    }

    // --- Helper: convierte entidad Review a ReviewResponseDTO ---
    private ReviewResponseDTO toDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .bookId(review.getBookId())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
