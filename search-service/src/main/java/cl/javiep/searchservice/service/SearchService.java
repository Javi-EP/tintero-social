package cl.javiep.searchservice.service;

import cl.javiep.searchservice.dto.*;
import cl.javiep.searchservice.entity.*;
import cl.javiep.searchservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookIndexRepository bookIndexRepository;
    private final UserIndexRepository userIndexRepository;

    // --- Búsqueda global (libros y usuarios) ---
    public SearchResultDTO globalSearch(String query) {
        List<BookIndexDTO> books = searchBooks(query);
        List<UserIndexDTO> users = searchUsers(query);

        return SearchResultDTO.builder()
                .books(books)
                .users(users)
                .build();
    }

    // --- Búsqueda filtrada de libros ---
    public List<BookIndexDTO> searchBooks(String query) {
        List<BookIndex> byTitle = bookIndexRepository
                .findByTitleContainingIgnoreCase(query);
        List<BookIndex> byAuthor = bookIndexRepository
                .findByAuthorContainingIgnoreCase(query);
        List<BookIndex> byGenre = bookIndexRepository
                .findByGenreContainingIgnoreCase(query);

        // Une los resultados y elimina duplicados por bookId
        return java.util.stream.Stream
                .concat(byTitle.stream(),
                        java.util.stream.Stream.concat(byAuthor.stream(), byGenre.stream()))
                .collect(Collectors.toMap(
                        BookIndex::getBookId,
                        b -> b,
                        (a, b) -> a))
                .values()
                .stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());
    }

    // --- Búsqueda de libros con filtros de género y rating ---
    public List<BookIndexDTO> searchBooksFiltered(String query, String genre, Double rating) {
        List<BookIndex> results = bookIndexRepository
                .findByTitleContainingIgnoreCase(query);

        if (genre != null && !genre.isEmpty()) {
            results = results.stream()
                    .filter(b -> b.getGenre() != null &&
                            b.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (rating != null) {
            results = results.stream()
                    .filter(b -> b.getRating() != null && b.getRating() >= rating)
                    .collect(Collectors.toList());
        }

        return results.stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());
    }

    // --- Búsqueda de usuarios ---
    public List<UserIndexDTO> searchUsers(String query) {
        return userIndexRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    // --- Indexar o actualizar un libro ---
    public void indexBook(BookIndexDTO dto) {
        BookIndex book = bookIndexRepository
                .findByBookId(dto.getBookId())
                .orElse(BookIndex.builder().bookId(dto.getBookId()).build());

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setTags(dto.getTags());
        book.setRating(dto.getRating());

        bookIndexRepository.save(book);
    }

    // --- Indexar o actualizar un usuario ---
    public void indexUser(UserIndexDTO dto) {
        UserIndex user = userIndexRepository
                .findByUserId(dto.getUserId())
                .orElse(UserIndex.builder().userId(dto.getUserId()).build());

        user.setName(dto.getName());
        user.setBio(dto.getBio());

        userIndexRepository.save(user);
    }

    // --- Helpers ---
    private BookIndexDTO toBookDTO(BookIndex b) {
        return BookIndexDTO.builder()
                .bookId(b.getBookId())
                .title(b.getTitle())
                .author(b.getAuthor())
                .genre(b.getGenre())
                .tags(b.getTags())
                .rating(b.getRating())
                .build();
    }

    private UserIndexDTO toUserDTO(UserIndex u) {
        return UserIndexDTO.builder()
                .userId(u.getUserId())
                .name(u.getName())
                .bio(u.getBio())
                .build();
    }
}