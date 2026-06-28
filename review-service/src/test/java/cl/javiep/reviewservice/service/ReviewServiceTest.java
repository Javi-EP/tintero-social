package cl.javiep.reviewservice.service;

import cl.javiep.reviewservice.dto.ReviewRequestDTO;
import cl.javiep.reviewservice.dto.ReviewResponseDTO;
import cl.javiep.reviewservice.dto.VoteRequestDTO;
import cl.javiep.reviewservice.entity.Review;
import cl.javiep.reviewservice.entity.Vote;
import cl.javiep.reviewservice.repository.ReviewRepository;
import cl.javiep.reviewservice.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview_shouldSave_whenDataIsValid() {
        ReviewRequestDTO dto = givenReviewRequestDTO();
        Review saved = givenReviewEntity(1L);
        when(reviewRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        ReviewResponseDTO result = reviewService.createReview(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRating()).isEqualTo(4);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBookId()).isEqualTo(10L);
    }

    @Test
    void createReview_shouldThrow_whenRatingIsInvalid() {
        ReviewRequestDTO dto = givenReviewRequestDTO();
        dto.setRating(6);

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void createReview_shouldThrow_whenDuplicate() {
        ReviewRequestDTO dto = givenReviewRequestDTO();
        when(reviewRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())).thenReturn(true);

        assertThatThrownBy(() -> reviewService.createReview(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void getReviewsByBook_shouldReturnList() {
        Review review = givenReviewEntity(1L);
        when(reviewRepository.findByBookId(10L)).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.getReviewsByBook(10L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(10L);
    }

    @Test
    void getReviewsByUser_shouldReturnList() {
        Review review = givenReviewEntity(1L);
        when(reviewRepository.findByUserId(1L)).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.getReviewsByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserId()).isEqualTo(1L);
    }

    @Test
    void updateReview_shouldModifyFields_whenOwner() {
        Review existing = givenReviewEntity(1L);
        existing.setTitle("Original");
        existing.setContent("Original content");
        existing.setRating(3);

        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setTitle("Actualizado");
        dto.setContent(null);
        dto.setRating(5);

        Review saved = givenReviewEntity(1L);
        saved.setTitle("Actualizado");
        saved.setRating(5);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(existing)).thenReturn(saved);

        ReviewResponseDTO result = reviewService.updateReview(1L, 1L, dto);

        assertThat(result.getTitle()).isEqualTo("Actualizado");
        assertThat(result.getRating()).isEqualTo(5);
        assertThat(existing.getContent()).isEqualTo("Original content");
    }

    @Test
    void updateReview_shouldThrow_whenNotOwner() {
        Review existing = givenReviewEntity(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> reviewService.updateReview(1L, 99L, new ReviewRequestDTO()))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.FORBIDDEN);
    }

    @Test
    void updateReview_shouldThrow_whenNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.updateReview(99L, 1L, new ReviewRequestDTO()))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteReview_shouldRemove_whenOwner() {
        Review existing = givenReviewEntity(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));

        reviewService.deleteReview(1L, 1L);

        verify(reviewRepository).delete(existing);
    }

    @Test
    void deleteReview_shouldThrow_whenNotOwner() {
        Review existing = givenReviewEntity(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> reviewService.deleteReview(1L, 99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.FORBIDDEN);

        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void voteReview_shouldSaveVote_whenValid() {
        VoteRequestDTO dto = new VoteRequestDTO();
        dto.setUserId(1L);

        when(reviewRepository.existsById(1L)).thenReturn(true);
        when(voteRepository.existsByUserIdAndReviewId(1L, 1L)).thenReturn(false);

        reviewService.voteReview(1L, dto);

        ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
        assertThat(captor.getValue().getReviewId()).isEqualTo(1L);
    }

    @Test
    void voteReview_shouldThrow_whenReviewNotFound() {
        VoteRequestDTO dto = new VoteRequestDTO();
        when(reviewRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.voteReview(99L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void voteReview_shouldThrow_whenDuplicateVote() {
        VoteRequestDTO dto = new VoteRequestDTO();
        dto.setUserId(1L);

        when(reviewRepository.existsById(1L)).thenReturn(true);
        when(voteRepository.existsByUserIdAndReviewId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> reviewService.voteReview(1L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    private static ReviewRequestDTO givenReviewRequestDTO() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(10L);
        dto.setRating(4);
        dto.setTitle("Buena lectura");
        dto.setContent("Me gustó mucho.");
        return dto;
    }

    private static Review givenReviewEntity(Long id) {
        return Review.builder()
                .id(id)
                .userId(1L)
                .bookId(10L)
                .rating(4)
                .title("Buena lectura")
                .content("Me gustó mucho.")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
