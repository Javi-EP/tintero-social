package cl.javiep.bookservice.service;

import cl.javiep.bookservice.dto.BookMapper;
import cl.javiep.bookservice.dto.BookRequestDTO;
import cl.javiep.bookservice.dto.BookResponseDTO;
import cl.javiep.bookservice.model.Book;
import cl.javiep.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void listAll_shouldReturnAllBooks_whenBooksExist() {
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.toResponseDTO(book1)).thenReturn(new BookResponseDTO());
        when(bookMapper.toResponseDTO(book2)).thenReturn(new BookResponseDTO());

        List<BookResponseDTO> result = bookService.listAll();

        assertThat(result).hasSize(2);
        verify(bookRepository).findAll();
    }

    @Test
    void listAll_shouldReturnEmptyList_whenNoBooks() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<BookResponseDTO> result = bookService.listAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        Book book = new Book();
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(1L);
        dto.setTitle("Existente");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponseDTO(book)).thenReturn(dto);

        BookResponseDTO result = bookService.findById(1L);

        assertThat(result.getTitle()).isEqualTo("Existente");
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Libro no encontrado");
    }

    @Test
    void save_shouldCreateBook_whenIsbnIsNotTaken() {
        BookRequestDTO dto = givenBookRequestDTO();
        Book entity = new Book();
        Book saved = new Book();
        BookResponseDTO expected = new BookResponseDTO();
        expected.setId(1L);
        expected.setTitle(dto.getTitle());

        when(bookRepository.findByIsbn(dto.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.toEntity(dto)).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(saved);
        when(bookMapper.toResponseDTO(saved)).thenReturn(expected);

        BookResponseDTO result = bookService.save(dto);

        assertThat(result.getTitle()).isEqualTo("Test Book");
        verify(bookRepository).save(entity);
    }

    @Test
    void save_shouldThrow_whenIsbnAlreadyExists() {
        BookRequestDTO dto = givenBookRequestDTO();
        when(bookRepository.findByIsbn(dto.getIsbn())).thenReturn(Optional.of(new Book()));

        assertThatThrownBy(() -> bookService.save(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ISBN");

        verify(bookRepository, never()).save(any());
    }

    @Test
    void update_shouldModifyAllFields_whenBookExists() {
        Book existing = new Book();
        existing.setId(1L);
        existing.setTitle("Original");

        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Actualizado");
        dto.setAuthor("Nuevo Autor");
        dto.setIsbn("999-9-99-999999-9");
        dto.setGenre("Nuevo Género");
        dto.setSynopsis("Nueva sinopsis");
        dto.setPublicationYear(2026);

        Book updated = new Book();
        BookResponseDTO expected = new BookResponseDTO();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(updated);
        when(bookMapper.toResponseDTO(updated)).thenReturn(expected);

        BookResponseDTO result = bookService.update(1L, dto);

        assertThat(result).isEqualTo(expected);
        assertThat(existing.getTitle()).isEqualTo("Actualizado");
        assertThat(existing.getAuthor()).isEqualTo("Nuevo Autor");
    }

    @Test
    void update_shouldThrow_whenBookNotFound() {
        BookRequestDTO dto = new BookRequestDTO();
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(99L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Libro no encontrado");
    }

    @Test
    void delete_shouldRemoveBook_whenExists() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.delete(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Libro no encontrado");

        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void findByTitle_shouldReturnMatchingBooks() {
        Book book = new Book();
        BookResponseDTO dto = new BookResponseDTO();
        dto.setTitle("Principito");

        when(bookRepository.findByTitleContainingIgnoreCase("principito")).thenReturn(List.of(book));
        when(bookMapper.toResponseDTO(book)).thenReturn(dto);

        List<BookResponseDTO> result = bookService.findByTitle("principito");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Principito");
    }

    private static BookRequestDTO givenBookRequestDTO() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Test Book");
        dto.setAuthor("Test Author");
        dto.setIsbn("978-3-16-148410-0");
        dto.setGenre("Test");
        dto.setSynopsis("Test synopsis");
        dto.setPublicationYear(2024);
        return dto;
    }
}
