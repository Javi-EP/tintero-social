package cl.javiep.notificationservice.controller;

import cl.javiep.notificationservice.dto.NotificationDTO;
import cl.javiep.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Operaciones para gestionar notificaciones internas de los usuarios")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @Operation(summary = "Crear notificación", description = "Crea una nueva notificación interna para un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @Parameter(description = "ID del usuario que recibe la notificación", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "ID del usuario que origina la notificación (opcional)", example = "2")
            @RequestParam(required = false) Long senderId,
            @Parameter(description = "Tipo de notificación", example = "NEW_FOLLOWER")
            @RequestParam String type,
            @Parameter(description = "Mensaje de la notificación", example = "Tienes un nuevo seguidor")
            @RequestParam String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, senderId, type, message));
    }

    @Operation(summary = "Listar notificaciones por usuario", description = "Obtiene todas las notificaciones de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getByUserId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @Operation(summary = "Marcar como leída", description = "Marca una notificación específica como leída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }
}