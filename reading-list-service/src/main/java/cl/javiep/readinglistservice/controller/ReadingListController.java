package cl.javiep.readinglistservice.controller;

import cl.javiep.readinglistservice.dto.*;
import cl.javiep.readinglistservice.service.ReadingListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ReadingListController {

    private final ReadingListService service;

    public ReadingListController(ReadingListService service) {
        this.service = service;
    }

    // POST /api/lists — crear nueva lista
    @PostMapping
    public ResponseEntity<ListResponseDTO> createList(@Valid @RequestBody ListRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createList(dto));
    }

    // GET /api/lists/user/{userId} — listas de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getListsByUser(userId));
    }

    // GET /api/lists/{id} — obtener lista por ID
    @GetMapping("/{id}")
    public ResponseEntity<ListResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getListById(id));
    }

    // DELETE /api/lists/{id} — eliminar lista
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        service.deleteList(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/lists/{id}/books — agregar libro a la lista
    @PostMapping("/{id}/books")
    public ResponseEntity<ListResponseDTO> addBook(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addBook(id, dto));
    }

    // PUT /api/lists/{listId}/books/{bookId}/progress — actualizar progreso
    @PutMapping("/{listId}/books/{bookId}/progress")
    public ResponseEntity<ListResponseDTO> updateProgress(
            @PathVariable Long listId,
            @PathVariable Long bookId,
            @RequestParam Integer progress) {
        return ResponseEntity.ok(service.updateProgress(listId, bookId, progress));
    }

    // DELETE /api/lists/{listId}/books/{bookId} — quitar libro de la lista
    @DeleteMapping("/{listId}/books/{bookId}")
    public ResponseEntity<Void> removeBook(
            @PathVariable Long listId,
            @PathVariable Long bookId) {
        service.removeBook(listId, bookId);
        return ResponseEntity.noContent().build();
    }
}