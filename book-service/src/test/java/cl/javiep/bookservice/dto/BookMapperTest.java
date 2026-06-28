package cl.javiep.bookservice.dto;

import cl.javiep.bookservice.model.Book;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    void toEntity_shouldMapAllFields_whenDtoIsValid() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("El Principito");
        dto.setAuthor("Antoine de Saint-Exupéry");
        dto.setIsbn("978-3-16-148410-0");
        dto.setGenre("Fantasía");
        dto.setSynopsis("Un cuento sobre un piloto y un principito.");
        dto.setPublicationYear(1943);

        Book book = mapper.toEntity(dto);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo("El Principito");
        assertThat(book.getAuthor()).isEqualTo("Antoine de Saint-Exupéry");
        assertThat(book.getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(book.getGenre()).isEqualTo("Fantasía");
        assertThat(book.getSynopsis()).isEqualTo("Un cuento sobre un piloto y un principito.");
        assertThat(book.getPublicationYear()).isEqualTo(1943);
    }

    @Test
    void toResponseDTO_shouldMapAllFields_whenBookIsValid() {
        Book book = new Book(1L, "1984", "George Orwell", "978-0-452-28423-4",
                "Ciencia Ficción", "Una distopía sobre un gobierno totalitario.", 1949);

        BookResponseDTO dto = mapper.toResponseDTO(book);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("1984");
        assertThat(dto.getAuthor()).isEqualTo("George Orwell");
        assertThat(dto.getIsbn()).isEqualTo("978-0-452-28423-4");
        assertThat(dto.getGenre()).isEqualTo("Ciencia Ficción");
        assertThat(dto.getSynopsis()).isEqualTo("Una distopía sobre un gobierno totalitario.");
        assertThat(dto.getPublicationYear()).isEqualTo(1949);
        assertThat(dto.getRatingPromedio()).isNull();
    }
}
