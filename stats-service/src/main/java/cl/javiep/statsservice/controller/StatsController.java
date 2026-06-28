package cl.javiep.statsservice.controller;

import cl.javiep.statsservice.dto.GlobalStatsDTO;
import cl.javiep.statsservice.dto.TopBookDTO;
import cl.javiep.statsservice.dto.UserStatsDTO;
import cl.javiep.statsservice.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stats", description = "Operaciones para estadísticas de lectura y métricas globales")
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Operation(summary = "Estadísticas de usuario", description = "Obtiene las estadísticas de lectura de un usuario específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.getUserStats(userId));
    }

    @Operation(summary = "Estadísticas globales", description = "Obtiene métricas globales de la plataforma")
    @ApiResponse(responseCode = "200", description = "Métricas globales obtenidas")
    @GetMapping("/global")
    public ResponseEntity<GlobalStatsDTO> getGlobalStats() {
        return ResponseEntity.ok(statsService.getGlobalStats());
    }

    @Operation(summary = "Top libros", description = "Obtiene los 10 libros más leídos de la plataforma")
    @ApiResponse(responseCode = "200", description = "Top libros obtenido correctamente")
    @GetMapping("/books/top")
    public ResponseEntity<List<TopBookDTO>> getTopBooks() {
        return ResponseEntity.ok(statsService.getTopBooks());
    }

    @Operation(summary = "Refrescar estadísticas", description = "Recalcula y actualiza las estadísticas de un usuario")
    @ApiResponse(responseCode = "200", description = "Estadísticas actualizadas correctamente")
    @PostMapping("/refresh/{userId}")
    public ResponseEntity<UserStatsDTO> refreshUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.getUserStats(userId));
    }
}