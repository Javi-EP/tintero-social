package cl.javiep.reviewservice.controller;

import cl.javiep.reviewservice.dto.*;
import cl.javiep.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Operaciones para gestionar reseñas de libros")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña para un libro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Rating inválido"),
            @ApiResponse(responseCode = "409", description = "Ya existe una reseña de este usuario para este libro")
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(dto));
    }

    @Operation(summary = "Listar reseñas por libro", description = "Obtiene todas las reseñas de un libro específico")
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas correctamente")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByBook(
            @Parameter(description = "ID del libro", example = "1")
            @PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    @Operation(summary = "Listar reseñas por usuario", description = "Obtiene todas las reseñas escritas por un usuario")
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas correctamente")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getByUser(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    @Operation(summary = "Editar reseña", description = "Edita una reseña existente, solo el autor puede editarla")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "403", description = "No puedes editar una reseña que no es tuya"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(
            @Parameter(description = "ID de la reseña", example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del usuario que edita", example = "1")
            @RequestParam Long userId,
            @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, userId, dto));
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña, solo el autor puede eliminarla")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "403", description = "No puedes eliminar una reseña que no es tuya"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la reseña", example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del usuario que elimina", example = "1")
            @RequestParam Long userId) {
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Votar reseña", description = "Marca una reseña como útil")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voto registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya votaste esta reseña")
    })
    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> vote(
            @Parameter(description = "ID de la reseña", example = "1")
            @PathVariable Long id,
            @RequestBody VoteRequestDTO dto) {
        reviewService.voteReview(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Health check", description = "Verifica que el servicio esté operativo")
    @ApiResponse(responseCode = "200", description = "Servicio operativo")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("review-service OK");
    }
}
