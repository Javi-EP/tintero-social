package cl.javiep.bookservice.controller;

import cl.javiep.bookservice.dto.BookRequestDTO;
import cl.javiep.bookservice.dto.BookResponseDTO;
import cl.javiep.bookservice.model.Book;
import cl.javiep.bookservice.service.BookService;
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

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operaciones para gestionar libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @Operation(summary = "Listar libros", description = "Obtiene todos los libros, opcionalmente filtrados por titulo")
    @ApiResponse(responseCode = "200", description = "Libros obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> list(
            @Parameter(description = "Titulo del libro para filtrar", example = "Principito")
            @RequestParam(required = false) String title) {

        if (title != null) {
            return ResponseEntity.ok(bookService.findByTitle(title));
        }
        return ResponseEntity.ok(bookService.listAll());
    }

    @Operation(summary = "Buscar libro por ID", description = "Obtiene un libro por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(summary = "Crear libro", description = "Crea un nuevo libro en el catalogo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> save(@Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(dto));
    }

    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @Operation(summary = "Eliminar libro", description = "Elimina un libro del catalogo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
