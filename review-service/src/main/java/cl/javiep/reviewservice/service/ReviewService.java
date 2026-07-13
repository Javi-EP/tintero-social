package cl.javiep.reviewservice.service;

import cl.javiep.reviewservice.dto.*;
import cl.javiep.reviewservice.entity.*;
import cl.javiep.reviewservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final VoteRepository voteRepository;

    // --- Crear reseña ---
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {
        log.info("Creando resena para libro {} del usuario {}", dto.getBookId(), dto.getUserId());

        // Validar que el rating esté entre 1 y 5
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            log.warn("Rating invalido: {} para el libro {}", dto.getRating(), dto.getBookId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rating must be between 1 and 5");
        }

        // Un usuario solo puede tener una reseña por libro
        if (reviewRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())) {
            log.warn("Resena duplicada para usuario {} y libro {}", dto.getUserId(), dto.getBookId());
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

        Review saved = reviewRepository.save(review);
        log.info("Resena creada con ID: {}", saved.getId());
        return toDTO(saved);
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
        log.info("Actualizando resena ID: {} por usuario {}", id, userId);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Resena no encontrada con ID: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Review not found");
                });

        // Solo el autor puede editar su reseña
        if (!review.getUserId().equals(userId)) {
            log.warn("Usuario {} intento editar resena {} sin ser el autor", userId, id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only edit your own reviews");
        }

        if (dto.getRating() != null) {
            if (dto.getRating() < 1 || dto.getRating() > 5) {
                log.warn("Rating invalido en actualizacion: {}", dto.getRating());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rating must be between 1 and 5");
            }
            review.setRating(dto.getRating());
        }

        if (dto.getTitle() != null) review.setTitle(dto.getTitle());
        if (dto.getContent() != null) review.setContent(dto.getContent());

        Review saved = reviewRepository.save(review);
        log.info("Resena {} actualizada correctamente", saved.getId());
        return toDTO(saved);
    }

    // --- Eliminar reseña ---
    public void deleteReview(Long id, Long userId) {
        log.info("Eliminando resena ID: {} por usuario {}", id, userId);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Resena no encontrada con ID: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Review not found");
                });

        // Solo el autor puede eliminar su reseña
        if (!review.getUserId().equals(userId)) {
            log.warn("Usuario {} intento eliminar resena {} sin ser el autor", userId, id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only delete your own reviews");
        }

        reviewRepository.delete(review);
        log.info("Resena {} eliminada correctamente", id);
    }

    // --- Votar reseña como útil ---
    public void voteReview(Long reviewId, VoteRequestDTO dto) {
        log.info("Votando resena {} por usuario {}", reviewId, dto.getUserId());

        // Verificar que la reseña exista
        if (!reviewRepository.existsById(reviewId)) {
            log.warn("Resena no encontrada para votar: {}", reviewId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Review not found");
        }

        // Evitar voto duplicado
        if (voteRepository.existsByUserIdAndReviewId(dto.getUserId(), reviewId)) {
            log.warn("Voto duplicado del usuario {} para resena {}", dto.getUserId(), reviewId);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You already voted this review");
        }

        Vote vote = Vote.builder()
                .userId(dto.getUserId())
                .reviewId(reviewId)
                .type("USEFUL")
                .build();

        voteRepository.save(vote);
        log.info("Voto registrado para resena {} por usuario {}", reviewId, dto.getUserId());
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
