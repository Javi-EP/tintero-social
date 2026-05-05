package cl.javiep.notificationservice.controller;

import cl.javiep.notificationservice.dto.NotificationDTO;
import cl.javiep.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @RequestParam Long userId,
            @RequestParam(required = false) Long senderId,
            @RequestParam String type,
            @RequestParam String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, senderId, type, message));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }
}