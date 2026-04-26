package cl.javiep.bookservice.dto;

import cl.javiep.bookservice.model.Book;
import org.springframework.stereotype.Component;

// @Component permite que Spring lo inyecte donde lo necesites
@Component
public class BookMapper {
    // Convierte RequestDTO → entidad (para guardar en la BD)
    public Book toEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setGenre(dto.getGenre());
        book.setSynopsis(dto.getSynopsis());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }

    // Convierte entidad → ResponseDTO (para enviar al cliente)
    public BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setGenre(book.getGenre());
        dto.setSynopsis(book.getSynopsis());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setRatingPromedio(null); // por ahora null, luego lo poblamos con review-service
        return dto;
    }
}
