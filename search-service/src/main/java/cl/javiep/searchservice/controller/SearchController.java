package cl.javiep.searchservice.controller;

import cl.javiep.searchservice.dto.*;
import cl.javiep.searchservice.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Search", description = "Operaciones de búsqueda global de libros y usuarios")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Búsqueda global", description = "Busca libros y usuarios por texto libre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados de búsqueda obtenidos")
    })
    @GetMapping
    public ResponseEntity<SearchResultDTO> globalSearch(
            @RequestParam String q) {
        return ResponseEntity.ok(searchService.globalSearch(q));
    }

    @Operation(summary = "Buscar libros", description = "Busca libros por texto con filtros opcionales de género y rating mínimo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libros encontrados")
    })
    @GetMapping("/books")
    public ResponseEntity<List<BookIndexDTO>> searchBooks(
            @RequestParam String q,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double rating) {
        return ResponseEntity.ok(searchService.searchBooksFiltered(q, genre, rating));
    }

    @Operation(summary = "Buscar usuarios", description = "Busca usuarios por nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserIndexDTO>> searchUsers(
            @RequestParam String q) {
        return ResponseEntity.ok(searchService.searchUsers(q));
    }

    @Operation(summary = "Indexar libro", description = "Indexa o actualiza un libro en el índice de búsqueda")
    @ApiResponse(responseCode = "201", description = "Libro indexado exitosamente")
    @PostMapping("/index/book")
    public ResponseEntity<Void> indexBook(@RequestBody BookIndexDTO dto) {
        searchService.indexBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Indexar usuario", description = "Indexa o actualiza un usuario en el índice de búsqueda")
    @ApiResponse(responseCode = "201", description = "Usuario indexado exitosamente")
    @PostMapping("/index/user")
    public ResponseEntity<Void> indexUser(@RequestBody UserIndexDTO dto) {
        searchService.indexUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Health check", description = "Verifica que el servicio de búsqueda esté operativo")
    @ApiResponse(responseCode = "200", description = "Servicio operativo")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("search-service OK");
    }
}