package cl.javiep.bookservice.controller;

import cl.javiep.bookservice.dto.BookRequestDTO;
import cl.javiep.bookservice.dto.BookResponseDTO;
import cl.javiep.bookservice.service.BookLinkAssembler;
import cl.javiep.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operaciones para gestionar libros")
public class BookController {

    private final BookService bookService;
    private final BookLinkAssembler linkAssembler;

    public BookController(BookService bookService, BookLinkAssembler linkAssembler) {
        this.bookService = bookService;
        this.linkAssembler = linkAssembler;
    }

    @Operation(summary = "Listar libros", description = "Obtiene todos los libros, opcionalmente filtrados por titulo. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "200", description = "Libros obtenidos correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BookResponseDTO>>> list(
            @Parameter(description = "Titulo del libro para filtrar", example = "Principito")
            @RequestParam(required = false) String title) {

        List<BookResponseDTO> books = (title != null)
                ? bookService.findByTitle(title)
                : bookService.listAll();

        List<EntityModel<BookResponseDTO>> models = books.stream()
                .map(linkAssembler::toModel)
                .toList();

        return ResponseEntity.ok(linkAssembler.toCollectionModel(models));
    }

    @Operation(summary = "Buscar libro por ID", description = "Obtiene un libro por su ID. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponseDTO>> findById(@PathVariable Long id) {
        BookResponseDTO book = bookService.findById(id);
        return ResponseEntity.ok(linkAssembler.toModel(book));
    }

    @Operation(summary = "Crear libro", description = "Crea un nuevo libro en el catalogo. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<BookResponseDTO>> save(@Valid @RequestBody BookRequestDTO dto) {
        BookResponseDTO book = bookService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkAssembler.toModel(book));
    }

    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {
        BookResponseDTO book = bookService.update(id, dto);
        return ResponseEntity.ok(linkAssembler.toModel(book));
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
