package cl.javiep.readinglistservice.controller;

import cl.javiep.readinglistservice.dto.*;
import cl.javiep.readinglistservice.service.ReadingListLinkAssembler;
import cl.javiep.readinglistservice.service.ReadingListService;
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
@RequestMapping("/api/lists")
@Tag(name = "Reading Lists", description = "Operaciones para gestionar listas de lectura personalizadas")
public class ReadingListController {

    private final ReadingListService service;
    private final ReadingListLinkAssembler linkAssembler;

    public ReadingListController(ReadingListService service, ReadingListLinkAssembler linkAssembler) {
        this.service = service;
        this.linkAssembler = linkAssembler;
    }

    @Operation(summary = "Crear lista", description = "Crea una nueva lista de lectura personalizada. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lista creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<ListResponseDTO>> createList(@Valid @RequestBody ListRequestDTO dto) {
        ListResponseDTO list = service.createList(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkAssembler.toModel(list));
    }

    @Operation(summary = "Listar listas por usuario", description = "Obtiene todas las listas de lectura de un usuario. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "200", description = "Listas obtenidas correctamente")
    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<ListResponseDTO>>> getByUser(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        List<EntityModel<ListResponseDTO>> lists = service.getListsByUser(userId).stream()
                .map(linkAssembler::toModel)
                .toList();
        return ResponseEntity.ok(linkAssembler.toCollectionModel(lists));
    }

    @Operation(summary = "Obtener lista por ID", description = "Obtiene una lista de lectura por su ID. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ListResponseDTO>> getById(
            @Parameter(description = "ID de la lista", example = "1")
            @PathVariable Long id) {
        ListResponseDTO list = service.getListById(id);
        return ResponseEntity.ok(linkAssembler.toModel(list));
    }

    @Operation(summary = "Eliminar lista", description = "Elimina una lista de lectura")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lista eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(
            @Parameter(description = "ID de la lista", example = "1")
            @PathVariable Long id) {
        service.deleteList(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar libro a lista", description = "Agrega un libro a una lista de lectura. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro agregado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    })
    @PostMapping("/{id}/books")
    public ResponseEntity<EntityModel<ListResponseDTO>> addBook(
            @Parameter(description = "ID de la lista", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDTO dto) {
        ListResponseDTO list = service.addBook(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkAssembler.toModel(list));
    }

    @Operation(summary = "Actualizar progreso", description = "Actualiza el progreso de lectura de un libro en una lista. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progreso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Lista o libro no encontrado")
    })
    @PutMapping("/{listId}/books/{bookId}/progress")
    public ResponseEntity<EntityModel<ListResponseDTO>> updateProgress(
            @Parameter(description = "ID de la lista", example = "1")
            @PathVariable Long listId,
            @Parameter(description = "ID del libro", example = "10")
            @PathVariable Long bookId,
            @Parameter(description = "Progreso 0-100", example = "75")
            @RequestParam Integer progress) {
        ListResponseDTO list = service.updateProgress(listId, bookId, progress);
        return ResponseEntity.ok(linkAssembler.toModel(list));
    }

    @Operation(summary = "Quitar libro de lista", description = "Elimina un libro de una lista de lectura")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado de la lista"),
            @ApiResponse(responseCode = "404", description = "Lista o libro no encontrado")
    })
    @DeleteMapping("/{listId}/books/{bookId}")
    public ResponseEntity<Void> removeBook(
            @Parameter(description = "ID de la lista", example = "1")
            @PathVariable Long listId,
            @Parameter(description = "ID del libro", example = "10")
            @PathVariable Long bookId) {
        service.removeBook(listId, bookId);
        return ResponseEntity.noContent().build();
    }
}
