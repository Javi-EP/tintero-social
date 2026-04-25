package cl.javiep.bookservice.service;

import cl.javiep.bookservice.model.Book;
import cl.javiep.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // Spring inyecta el repositorio automáticamente (inyección de dependencias)
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Retorna todos los libros
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    // Busca por ID; si no existe lanza una excepción con mensaje claro
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }

    // Guarda un libro nuevo; verifica que el ISBN no esté duplicado
    public Book save(Book book) {
        if (book.getIsbn() != null && bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + book.getIsbn());
        }
        return bookRepository.save(book);
    }

    // Actualiza solo los campos que lleguen en el request
    public Book update(Long id, Book newData) {
        Book currentBook = findById(id); // reutiliza el metodo de arriba

        currentBook.setTitle(newData.getTitle());
        currentBook.setAuthor(newData.getAuthor());
        currentBook.setIsbn(newData.getIsbn());
        currentBook.setGenre(newData.getGenre());
        currentBook.setSynopsis(newData.getSynopsis());
        currentBook.setPublicationYear(newData.getPublicationYear());

        return bookRepository.save(currentBook);
    }

    // Elimina un libro por ID; verifica que exista antes de borrar
    public void delete(Long id) {
        findById(id); // si no existe, lanza excepción antes de intentar borrar
        bookRepository.deleteById(id);
    }

    // Busca libros por título (búsqueda parcial)
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
