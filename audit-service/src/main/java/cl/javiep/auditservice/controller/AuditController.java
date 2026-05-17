package cl.javiep.auditservice.controller;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.service.AuditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/event")
    public ResponseEntity<AuditEventResponseDTO> registerEvent(@Valid @RequestBody AuditEventRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.registerEvent(dto));
    }

    @GetMapping("/events")
    public ResponseEntity<List<AuditEventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(auditService.getAllEvents());
    }

    @GetMapping("/events/user/{userId}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(auditService.getEventsByUser(userId));
    }

    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(auditService.getEventsByType(eventType));
    }

    @GetMapping("/events/resource/{resourceType}/{resourceId}")
    public ResponseEntity<List<AuditEventResponseDTO>> getEventsByResource(
            @PathVariable String resourceType,
            @PathVariable Long resourceId) {
        return ResponseEntity.ok(auditService.getEventsByResource(resourceId, resourceType));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        auditService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}