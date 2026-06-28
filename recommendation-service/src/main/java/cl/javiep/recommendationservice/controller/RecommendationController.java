package cl.javiep.recommendationservice.controller;

import cl.javiep.recommendationservice.dto.*;
import cl.javiep.recommendationservice.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Operaciones para gestionar recomendaciones personalizadas de libros")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "Obtener recomendaciones", description = "Obtiene las recomendaciones activas (no descartadas) de un usuario")
    @ApiResponse(responseCode = "200", description = "Recomendaciones obtenidas correctamente")
    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendations(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }

    @Operation(summary = "Regenerar recomendaciones", description = "Regenera las recomendaciones de un usuario basadas en sus preferencias de género")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recomendaciones regeneradas correctamente"),
            @ApiResponse(responseCode = "400", description = "El usuario no tiene preferencias de género")
    })
    @PostMapping("/refresh/{userId}")
    public ResponseEntity<Void> refresh(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        recommendationService.refreshRecommendations(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Descartar recomendación", description = "Descarta una recomendación para que no vuelva a aparecer (RF-17)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Recomendación descartada correctamente"),
            @ApiResponse(responseCode = "404", description = "Recomendación no encontrada")
    })
    @PostMapping("/{userId}/dismiss/{bookId}")
    public ResponseEntity<Void> dismiss(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "ID del libro a descartar", example = "10")
            @PathVariable Long bookId) {
        recommendationService.dismissRecommendation(userId, bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Libros en tendencia", description = "Obtiene los libros con mayor puntuación a nivel global")
    @ApiResponse(responseCode = "200", description = "Tendencias obtenidas correctamente")
    @GetMapping("/trending")
    public ResponseEntity<List<RecommendationResponseDTO>> getTrending() {
        return ResponseEntity.ok(recommendationService.getTrending());
    }

    @Operation(summary = "Agregar preferencia de género", description = "Registra que un usuario prefiere un género específico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Preferencia agregada correctamente"),
            @ApiResponse(responseCode = "409", description = "La preferencia ya existe para este usuario")
    })
    @PostMapping("/preferences")
    public ResponseEntity<Void> addGenrePreference(
            @Valid @RequestBody GenrePreferenceDTO dto) {
        recommendationService.addGenrePreference(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Health check", description = "Verifica que el servicio esté operativo")
    @ApiResponse(responseCode = "200", description = "Servicio operativo")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("recommendation-service OK");
    }
}
