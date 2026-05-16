package cl.javiep.recommendationservice.service;

import cl.javiep.recommendationservice.dto.*;
import cl.javiep.recommendationservice.entity.*;
import cl.javiep.recommendationservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final GenrePreferenceRepository genrePreferenceRepository;

    // --- Obtener recomendaciones de un usuario ---
    public List<RecommendationResponseDTO> getRecommendations(Long userId) {
        return recommendationRepository.findByUserIdAndDismissedFalse(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Agregar una recomendación ---
    public RecommendationResponseDTO addRecommendation(RecommendationRequestDTO dto) {
        if (recommendationRepository.existsByUserIdAndBookId(dto.getUserId(), dto.getBookId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Recommendation already exists for this user and book");
        }

        Recommendation recommendation = Recommendation.builder()
                .userId(dto.getUserId())
                .bookId(dto.getBookId())
                .score(dto.getScore())
                .reason(dto.getReason())
                .build();

        return toDTO(recommendationRepository.save(recommendation));
    }

    // --- Descartar una recomendación (RF-17) ---
    public void dismissRecommendation(Long userId, Long bookId) {
        List<Recommendation> list = recommendationRepository
                .findByUserIdAndDismissedFalse(userId);

        Recommendation recommendation = list.stream()
                .filter(r -> r.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recommendation not found"));

        recommendation.setDismissed(true);
        recommendationRepository.save(recommendation);
    }

    // --- Regenerar recomendaciones ---
    public void refreshRecommendations(Long userId) {
        List<GenrePreference> preferences =
                genrePreferenceRepository.findByUserId(userId);

        if (preferences.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User has no genre preferences to generate recommendations");
        }
    }

    // --- Libros en tendencia global ---
    public List<RecommendationResponseDTO> getTrending() {
        List<Recommendation> all = recommendationRepository.findAll();

        return all.stream()
                .filter(r -> !r.getDismissed())
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(10)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Agregar preferencia de género ---
    public void addGenrePreference(GenrePreferenceDTO dto) {
        if (genrePreferenceRepository.existsByUserIdAndGenreId(
                dto.getUserId(), dto.getGenreId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Genre preference already exists for this user");
        }

        GenrePreference preference = GenrePreference.builder()
                .userId(dto.getUserId())
                .genreId(dto.getGenreId())
                .weight(dto.getWeight())
                .build();

        genrePreferenceRepository.save(preference);
    }

    // --- Helper: entidad a DTO ---
    private RecommendationResponseDTO toDTO(Recommendation r) {
        return RecommendationResponseDTO.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .bookId(r.getBookId())
                .score(r.getScore())
                .reason(r.getReason())
                .dismissed(r.getDismissed())
                .createdAt(r.getCreatedAt())
                .build();
    }
}