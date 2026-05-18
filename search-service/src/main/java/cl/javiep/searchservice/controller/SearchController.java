package cl.javiep.searchservice.controller;

import cl.javiep.searchservice.dto.*;
import cl.javiep.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // GET /api/search?q={query}
    @GetMapping
    public ResponseEntity<SearchResultDTO> globalSearch(
            @RequestParam String q) {
        return ResponseEntity.ok(searchService.globalSearch(q));
    }

    // GET /api/search/books?q={query}&genre={g}&rating={r}
    @GetMapping("/books")
    public ResponseEntity<List<BookIndexDTO>> searchBooks(
            @RequestParam String q,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double rating) {
        return ResponseEntity.ok(searchService.searchBooksFiltered(q, genre, rating));
    }

    // GET /api/search/users?q={query}
    @GetMapping("/users")
    public ResponseEntity<List<UserIndexDTO>> searchUsers(
            @RequestParam String q) {
        return ResponseEntity.ok(searchService.searchUsers(q));
    }

    // POST /api/search/index/book
    @PostMapping("/index/book")
    public ResponseEntity<Void> indexBook(@RequestBody BookIndexDTO dto) {
        searchService.indexBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // POST /api/search/index/user
    @PostMapping("/index/user")
    public ResponseEntity<Void> indexUser(@RequestBody UserIndexDTO dto) {
        searchService.indexUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /api/search/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("search-service OK");
    }
}