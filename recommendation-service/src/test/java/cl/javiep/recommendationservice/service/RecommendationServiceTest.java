package cl.javiep.recommendationservice.service;

import cl.javiep.recommendationservice.dto.GenrePreferenceDTO;
import cl.javiep.recommendationservice.dto.RecommendationRequestDTO;
import cl.javiep.recommendationservice.dto.RecommendationResponseDTO;
import cl.javiep.recommendationservice.entity.GenrePreference;
import cl.javiep.recommendationservice.entity.Recommendation;
import cl.javiep.recommendationservice.repository.GenrePreferenceRepository;
import cl.javiep.recommendationservice.repository.RecommendationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private GenrePreferenceRepository genrePreferenceRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    @Test
    void getRecommendations_shouldReturnList_whenExists() {
        Recommendation rec = givenRecommendation(1L, 1L, 10L, 0.9);
        when(recommendationRepository.findByUserIdAndDismissedFalse(1L))
                .thenReturn(List.of(rec));

        List<RecommendationResponseDTO> result = recommendationService.getRecommendations(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserId()).isEqualTo(1L);
        assertThat(result.getFirst().getBookId()).isEqualTo(10L);
    }

    @Test
    void getRecommendations_shouldReturnEmptyList_whenNone() {
        when(recommendationRepository.findByUserIdAndDismissedFalse(1L))
                .thenReturn(List.of());

        List<RecommendationResponseDTO> result = recommendationService.getRecommendations(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void addRecommendation_shouldSave_whenNotDuplicate() {
        RecommendationRequestDTO dto = givenRecommendationRequestDTO();
        Recommendation saved = givenRecommendation(1L, 1L, 10L, 0.9);

        when(recommendationRepository.existsByUserIdAndBookId(1L, 10L))
                .thenReturn(false);
        when(recommendationRepository.save(any(Recommendation.class)))
                .thenReturn(saved);

        RecommendationResponseDTO result = recommendationService.addRecommendation(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBookId()).isEqualTo(10L);
    }

    @Test
    void addRecommendation_shouldThrow_whenDuplicate() {
        RecommendationRequestDTO dto = givenRecommendationRequestDTO();

        when(recommendationRepository.existsByUserIdAndBookId(1L, 10L))
                .thenReturn(true);

        assertThatThrownBy(() -> recommendationService.addRecommendation(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

        verify(recommendationRepository, never()).save(any());
    }

    @Test
    void dismissRecommendation_shouldSetDismissed_whenFound() {
        Recommendation rec = givenRecommendation(1L, 1L, 10L, 0.9);
        rec.setDismissed(false);

        when(recommendationRepository.findByUserIdAndDismissedFalse(1L))
                .thenReturn(List.of(rec));

        recommendationService.dismissRecommendation(1L, 10L);

        assertThat(rec.getDismissed()).isTrue();
        verify(recommendationRepository).save(rec);
    }

    @Test
    void dismissRecommendation_shouldThrow_whenNotFound() {
        when(recommendationRepository.findByUserIdAndDismissedFalse(1L))
                .thenReturn(List.of());

        assertThatThrownBy(() -> recommendationService.dismissRecommendation(1L, 99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verify(recommendationRepository, never()).save(any());
    }

    @Test
    void dismissRecommendation_shouldFindCorrectBook_amongMultiple() {
        Recommendation rec1 = givenRecommendation(1L, 1L, 10L, 0.9);
        Recommendation rec2 = givenRecommendation(2L, 1L, 20L, 0.8);

        when(recommendationRepository.findByUserIdAndDismissedFalse(1L))
                .thenReturn(List.of(rec1, rec2));

        recommendationService.dismissRecommendation(1L, 20L);

        assertThat(rec1.getDismissed()).isFalse();
        assertThat(rec2.getDismissed()).isTrue();
        verify(recommendationRepository).save(rec2);
    }

    @Test
    void refreshRecommendations_shouldThrow_whenNoGenrePreferences() {
        when(genrePreferenceRepository.findByUserId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> recommendationService.refreshRecommendations(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void refreshRecommendations_shouldNotThrow_whenPreferencesExist() {
        GenrePreference pref = new GenrePreference();
        pref.setUserId(1L);
        when(genrePreferenceRepository.findByUserId(1L)).thenReturn(List.of(pref));

        recommendationService.refreshRecommendations(1L);

        verify(genrePreferenceRepository).findByUserId(1L);
    }

    @Test
    void getTrending_shouldReturnTop10SortedByScore() {
        Recommendation rec1 = givenRecommendation(1L, 1L, 10L, 0.5);
        Recommendation rec2 = givenRecommendation(2L, 1L, 20L, 0.9);
        Recommendation rec3 = givenRecommendation(3L, 1L, 30L, 0.7);
        rec2.setDismissed(true);

        when(recommendationRepository.findAll()).thenReturn(List.of(rec1, rec2, rec3));

        List<RecommendationResponseDTO> result = recommendationService.getTrending();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getScore()).isEqualTo(0.7);
        assertThat(result.get(1).getScore()).isEqualTo(0.5);
    }

    @Test
    void getTrending_shouldReturnEmpty_whenNone() {
        when(recommendationRepository.findAll()).thenReturn(List.of());

        List<RecommendationResponseDTO> result = recommendationService.getTrending();

        assertThat(result).isEmpty();
    }

    @Test
    void addGenrePreference_shouldSave_whenNotDuplicate() {
        GenrePreferenceDTO dto = new GenrePreferenceDTO();
        dto.setUserId(1L);
        dto.setGenreId(3L);
        dto.setWeight(0.8);

        when(genrePreferenceRepository.existsByUserIdAndGenreId(1L, 3L))
                .thenReturn(false);

        recommendationService.addGenrePreference(dto);

        verify(genrePreferenceRepository).save(any(GenrePreference.class));
    }

    @Test
    void addGenrePreference_shouldThrow_whenDuplicate() {
        GenrePreferenceDTO dto = new GenrePreferenceDTO();
        dto.setUserId(1L);
        dto.setGenreId(3L);

        when(genrePreferenceRepository.existsByUserIdAndGenreId(1L, 3L))
                .thenReturn(true);

        assertThatThrownBy(() -> recommendationService.addGenrePreference(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

        verify(genrePreferenceRepository, never()).save(any());
    }

    private static Recommendation givenRecommendation(Long id, Long userId, Long bookId, double score) {
        return Recommendation.builder()
                .id(id)
                .userId(userId)
                .bookId(bookId)
                .score(score)
                .reason("Test reason")
                .dismissed(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static RecommendationRequestDTO givenRecommendationRequestDTO() {
        RecommendationRequestDTO dto = new RecommendationRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(10L);
        dto.setScore(0.9);
        dto.setReason("Test reason");
        return dto;
    }
}
