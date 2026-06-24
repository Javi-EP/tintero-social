package cl.javiep.auditservice.controller;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "Operaciones para gestionar eventos de auditoría del sistema")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @Operation(summary = "Registrar evento", description = "Registra un nuevo evento de auditoría en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del evento inválidos")
    })
    @PostMapping("/event")
    public ResponseEntity<AuditEventResponseDTO> registerEvent(@Valid @RequestBody AuditEventRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.registerEvent(dto));
    }

    @Operation(summary = "Listar todos los eventos", description = "Obtiene todos los eventos de auditoría registrados")
    @ApiResponse(responseCode = "200", description = "Eventos obtenidos correctamente")
    @GetMapping("/events")
    public ResponseEntity<List<AuditEventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(auditService.getAllEvents());
    }

    @Operation(summary = "Listar eventos por usuario", description = "Obtiene todos los eventos de auditoría generados por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Eventos obtenidos correctamente")
    @GetMapping("/events/user/{userId}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByUser(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(auditService.getEventsByUser(userId));
    }

    @Operation(summary = "Listar eventos por tipo", description = "Obtiene todos los eventos de auditoría de un tipo específico")
    @ApiResponse(responseCode = "200", description = "Eventos obtenidos correctamente")
    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByType(
            @Parameter(description = "Tipo de evento", example = "LOGIN")
            @PathVariable String eventType) {
        return ResponseEntity.ok(auditService.getEventsByType(eventType));
    }

    @Operation(summary = "Listar eventos por recurso", description = "Obtiene todos los eventos relacionados a un recurso específico")
    @ApiResponse(responseCode = "200", description = "Eventos obtenidos correctamente")
    @GetMapping("/events/resource/{resourceType}/{resourceId}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByResource(
            @Parameter(description = "Tipo de recurso", example = "BOOK")
            @PathVariable String resourceType,
            @Parameter(description = "ID del recurso", example = "10")
            @PathVariable Long resourceId) {
        return ResponseEntity.ok(auditService.getEventsByResource(resourceId, resourceType));
    }

    @Operation(summary = "Eliminar evento", description = "Elimina un evento de auditoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID del evento", example = "1")
            @PathVariable Long id) {
        auditService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}