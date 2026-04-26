package cl.javiep.bookservice.controller;

import cl.javiep.bookservice.dto.BookRequestDTO;
import cl.javiep.bookservice.dto.BookResponseDTO;
import cl.javiep.bookservice.model.Book;
import cl.javiep.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RequestMapping define la ruta base de todos los endpoints de esta clase
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // GET /api/books → lista todos los libros
    // También acepta GET /api/books?title=harry para filtrar
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> list(
            @RequestParam(required = false) String title) {

        if (title != null) {
            return ResponseEntity.ok(bookService.findByTitle(title));
        }
        return ResponseEntity.ok(bookService.listAll());
    }

    // GET /api/books/{id} → retorna un libro por su ID
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // POST /api/books → crea un nuevo libro
    // @Valid activa las validaciones del modelo (@NotBlank, etc.)
    // @RequestBody convierte el JSON del request en un objeto Book
    @PostMapping
    public ResponseEntity<BookResponseDTO> save(@Valid @RequestBody BookRequestDTO dto) {
        // HTTP 201 Created es más correcto que 200 OK para creaciones
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(dto));
    }

    // PUT /api/books/{id} → actualiza un libro existente
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    // DELETE /api/books/{id} → elimina un libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        // HTTP 204 No Content: operación exitosa pero sin cuerpo en la respuesta
        return ResponseEntity.noContent().build();
    }
}
