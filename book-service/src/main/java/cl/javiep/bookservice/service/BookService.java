package cl.javiep.bookservice.service;

import cl.javiep.bookservice.dto.BookMapper;
import cl.javiep.bookservice.dto.BookRequestDTO;
import cl.javiep.bookservice.dto.BookResponseDTO;
import cl.javiep.bookservice.model.Book;
import cl.javiep.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    // Spring inyecta el repositorio automáticamente (inyección de dependencias)
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    // Retorna todos los libros
    public List<BookResponseDTO> listAll() {
        return bookRepository.findAll()
                .stream()
                //Por cada Book en la lista, lo convierte a ResponseDTO
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Busca por ID; si no existe lanza una excepción con mensaje claro
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: "+id));
        return bookMapper.toResponseDTO(book);
    }

    // Guarda un libro nuevo; verifica que el ISBN no esté duplicado
    public BookResponseDTO save(BookRequestDTO dto) {
        if (dto.getIsbn() != null && bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + dto.getIsbn());
        }
        Book book = bookMapper.toEntity(dto);       // DTO → entidad
        Book saved = bookRepository.save(book);  // guarda en BD
        return bookMapper.toResponseDTO(saved);  // entidad → ResponseDTO
    }

    // Actualiza solo los campos que lleguen en el request
    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        Book currentBook = bookRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Libro no encontrado con ID: "+id));

        currentBook.setTitle(dto.getTitle());
        currentBook.setAuthor(dto.getAuthor());
        currentBook.setIsbn(dto.getIsbn());
        currentBook.setGenre(dto.getGenre());
        currentBook.setSynopsis(dto.getSynopsis());
        currentBook.setPublicationYear(dto.getPublicationYear());

        return bookMapper.toResponseDTO(bookRepository.save(currentBook));
    }

    // Elimina un libro por ID; verifica que exista antes de borrar
    public void delete(Long id) {
        bookRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Libro no encontrado con ID: "+id)); // si no existe, lanza excepción antes de intentar borrar
        bookRepository.deleteById(id);
    }

    // Busca libros por título (búsqueda parcial)
    public List<BookResponseDTO> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
