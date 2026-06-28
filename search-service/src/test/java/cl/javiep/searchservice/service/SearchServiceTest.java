package cl.javiep.searchservice.service;

import cl.javiep.searchservice.dto.BookIndexDTO;
import cl.javiep.searchservice.dto.SearchResultDTO;
import cl.javiep.searchservice.dto.UserIndexDTO;
import cl.javiep.searchservice.entity.BookIndex;
import cl.javiep.searchservice.entity.UserIndex;
import cl.javiep.searchservice.repository.BookIndexRepository;
import cl.javiep.searchservice.repository.UserIndexRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private BookIndexRepository bookIndexRepository;

    @Mock
    private UserIndexRepository userIndexRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void searchBooks_shouldReturnCombinedDeduplicatedResults() {
        BookIndex book1 = BookIndex.builder().bookId(1L).title("Cien Años").author("García").build();
        BookIndex book2 = BookIndex.builder().bookId(2L).title("El Amor").author("García").build();

        when(bookIndexRepository.findByTitleContainingIgnoreCase("García"))
                .thenReturn(List.of(book1));
        when(bookIndexRepository.findByAuthorContainingIgnoreCase("García"))
                .thenReturn(List.of(book1, book2));
        when(bookIndexRepository.findByGenreContainingIgnoreCase("García"))
                .thenReturn(List.of());

        List<BookIndexDTO> result = searchService.searchBooks("García");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(BookIndexDTO::getBookId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void searchBooks_shouldReturnEmpty_whenNoMatch() {
        when(bookIndexRepository.findByTitleContainingIgnoreCase("xyz"))
                .thenReturn(List.of());
        when(bookIndexRepository.findByAuthorContainingIgnoreCase("xyz"))
                .thenReturn(List.of());
        when(bookIndexRepository.findByGenreContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<BookIndexDTO> result = searchService.searchBooks("xyz");

        assertThat(result).isEmpty();
    }

    @Test
    void searchUsers_shouldReturnMatchingUsers() {
        UserIndex user = UserIndex.builder().userId(1L).name("Juan Pérez").build();
        when(userIndexRepository.findByNameContainingIgnoreCase("Juan"))
                .thenReturn(List.of(user));

        List<UserIndexDTO> result = searchService.searchUsers("Juan");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Juan Pérez");
    }

    @Test
    void searchUsers_shouldReturnEmpty_whenNoMatch() {
        when(userIndexRepository.findByNameContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<UserIndexDTO> result = searchService.searchUsers("xyz");

        assertThat(result).isEmpty();
    }

    @Test
    void globalSearch_shouldCombineBooksAndUsers() {
        BookIndex book = BookIndex.builder().bookId(1L).title("Java").build();
        UserIndex user = UserIndex.builder().userId(1L).name("Java Lover").build();

        when(bookIndexRepository.findByTitleContainingIgnoreCase("Java"))
                .thenReturn(List.of(book));
        when(bookIndexRepository.findByAuthorContainingIgnoreCase("Java"))
                .thenReturn(List.of());
        when(bookIndexRepository.findByGenreContainingIgnoreCase("Java"))
                .thenReturn(List.of());
        when(userIndexRepository.findByNameContainingIgnoreCase("Java"))
                .thenReturn(List.of(user));

        SearchResultDTO result = searchService.globalSearch("Java");

        assertThat(result.getBooks()).hasSize(1);
        assertThat(result.getUsers()).hasSize(1);
    }

    @Test
    void searchBooksFiltered_shouldFilterByGenre() {
        BookIndex book1 = BookIndex.builder().bookId(1L).title("Fantasía").genre("FANTASY").rating(4.5).build();
        BookIndex book2 = BookIndex.builder().bookId(2L).title("Ciencia").genre("SCI_FI").rating(4.0).build();

        when(bookIndexRepository.findByTitleContainingIgnoreCase("a"))
                .thenReturn(List.of(book1, book2));

        List<BookIndexDTO> result = searchService.searchBooksFiltered("a", "FANTASY", null);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(1L);
    }

    @Test
    void searchBooksFiltered_shouldFilterByRating() {
        BookIndex book1 = BookIndex.builder().bookId(1L).title("Fantasía").genre("FANTASY").rating(4.5).build();
        BookIndex book2 = BookIndex.builder().bookId(2L).title("Ciencia").genre("SCI_FI").rating(4.0).build();

        when(bookIndexRepository.findByTitleContainingIgnoreCase("a"))
                .thenReturn(List.of(book1, book2));

        List<BookIndexDTO> result = searchService.searchBooksFiltered("a", null, 4.2);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(1L);
    }

    @Test
    void searchBooksFiltered_shouldFilterByGenreAndRating() {
        BookIndex book1 = BookIndex.builder().bookId(1L).title("Fantasía").genre("FANTASY").rating(4.5).build();
        BookIndex book2 = BookIndex.builder().bookId(2L).title("Más Fantasía").genre("FANTASY").rating(3.5).build();

        when(bookIndexRepository.findByTitleContainingIgnoreCase("Fantasía"))
                .thenReturn(List.of(book1, book2));

        List<BookIndexDTO> result = searchService.searchBooksFiltered("Fantasía", "FANTASY", 4.0);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(1L);
    }

    @Test
    void searchBooksFiltered_shouldReturnEmpty_whenNoMatch() {
        when(bookIndexRepository.findByTitleContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<BookIndexDTO> result = searchService.searchBooksFiltered("xyz", null, null);

        assertThat(result).isEmpty();
    }

    @Test
    void indexBook_shouldCreateNew_whenNotExists() {
        BookIndexDTO dto = BookIndexDTO.builder()
                .bookId(1L).title("Nuevo Libro").author("Autor").genre("FICTION").build();

        when(bookIndexRepository.findByBookId(1L)).thenReturn(Optional.empty());
        when(bookIndexRepository.save(any(BookIndex.class))).thenAnswer(i -> i.getArgument(0));

        searchService.indexBook(dto);

        verify(bookIndexRepository).save(any(BookIndex.class));
    }

    @Test
    void indexBook_shouldUpdateExisting_whenExists() {
        BookIndex existing = BookIndex.builder().id(1L).bookId(1L).title("Viejo").build();
        BookIndexDTO dto = BookIndexDTO.builder()
                .bookId(1L).title("Nuevo Título").author("Autor").build();

        when(bookIndexRepository.findByBookId(1L)).thenReturn(Optional.of(existing));
        when(bookIndexRepository.save(any(BookIndex.class))).thenAnswer(i -> i.getArgument(0));

        searchService.indexBook(dto);

        assertThat(existing.getTitle()).isEqualTo("Nuevo Título");
        verify(bookIndexRepository).save(existing);
    }

    @Test
    void indexUser_shouldCreateNew_whenNotExists() {
        UserIndexDTO dto = UserIndexDTO.builder().userId(1L).name("Juan").bio("Lector").build();

        when(userIndexRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userIndexRepository.save(any(UserIndex.class))).thenAnswer(i -> i.getArgument(0));

        searchService.indexUser(dto);

        verify(userIndexRepository).save(any(UserIndex.class));
    }

    @Test
    void indexUser_shouldUpdateExisting_whenExists() {
        UserIndex existing = UserIndex.builder().id(1L).userId(1L).name("Viejo").build();
        UserIndexDTO dto = UserIndexDTO.builder().userId(1L).name("Nuevo Nombre").bio("Nueva bio").build();

        when(userIndexRepository.findByUserId(1L)).thenReturn(Optional.of(existing));
        when(userIndexRepository.save(any(UserIndex.class))).thenAnswer(i -> i.getArgument(0));

        searchService.indexUser(dto);

        assertThat(existing.getName()).isEqualTo("Nuevo Nombre");
        assertThat(existing.getBio()).isEqualTo("Nueva bio");
        verify(userIndexRepository).save(existing);
    }
}
