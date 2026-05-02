package cl.javiep.readinglistservice.service;

import cl.javiep.readinglistservice.client.BookClient;
import cl.javiep.readinglistservice.client.UserClient;
import cl.javiep.readinglistservice.dto.*;
import cl.javiep.readinglistservice.mapper.ReadingListMapper;
import cl.javiep.readinglistservice.model.ReadingList;
import cl.javiep.readinglistservice.model.ReadingListItem;
import cl.javiep.readinglistservice.repository.ReadingListItemRepository;
import cl.javiep.readinglistservice.repository.ReadingListRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingListService {

    private final ReadingListRepository listRepository;
    private final ReadingListItemRepository itemRepository;
    private final ReadingListMapper mapper;
    private final BookClient bookClient;
    private final UserClient userClient;

    public ReadingListService(ReadingListRepository listRepository,
                              ReadingListItemRepository itemRepository,
                              ReadingListMapper mapper,
                              BookClient bookClient,
                              UserClient userClient) {
        this.listRepository = listRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.bookClient = bookClient;
        this.userClient = userClient;
    }

    // Crear lista — verifica que el usuario exista en user-service
    public ListResponseDTO createList(ListRequestDTO dto) {
        if (!userClient.userExists(dto.getUserId())) {
            throw new RuntimeException("Usuario no encontrado con ID: " + dto.getUserId());
        }
        ReadingList list = mapper.toEntity(dto);
        return mapper.toResponseDTO(listRepository.save(list));
    }

    // Obtener todas las listas de un usuario
    public List<ListResponseDTO> getListsByUser(Long userId) {
        return listRepository.findByUserId(userId)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener una lista por ID
    public ListResponseDTO getListById(Long id) {
        ReadingList list = listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada con ID: " + id));
        return mapper.toResponseDTO(list);
    }

    // Eliminar lista
    public void deleteList(Long id) {
        listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada con ID: " + id));
        listRepository.deleteById(id);
    }

    // Agregar libro a una lista — verifica que el libro exista en book-service
    public ListResponseDTO addBook(Long listId, ItemRequestDTO dto) {
        ReadingList list = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada con ID: " + listId));

        // ← Comunicación inter-servicio: pregunta a book-service
        if (!bookClient.bookExists(dto.getBookId())) {
            throw new RuntimeException("Libro no encontrado con ID: " + dto.getBookId());
        }

        // Verifica que el libro no esté ya en la lista
        if (itemRepository.existsByReadingListIdAndBookId(listId, dto.getBookId())) {
            throw new RuntimeException("El libro ya está en esta lista");
        }

        ReadingListItem item = mapper.toItemEntity(dto);
        item.setReadingList(list);
        list.getItems().add(item);

        return mapper.toResponseDTO(listRepository.save(list));
    }

    // Actualizar progreso de un libro en la lista
    public ListResponseDTO updateProgress(Long listId, Long bookId, Integer progress) {
        ReadingListItem item = itemRepository
                .findByReadingListIdAndBookId(listId, bookId)
                .orElseThrow(() -> new RuntimeException("El libro no está en esta lista"));

        item.setProgress(progress);

        // Si el progreso llega a 100, registrar fecha de finalización
        if (progress == 100) {
            item.setFinishedAt(LocalDateTime.now());
        }

        itemRepository.save(item);

        ReadingList list = listRepository.findById(listId).get();
        return mapper.toResponseDTO(list);
    }

    // Eliminar libro de una lista
    public void removeBook(Long listId, Long bookId) {
        ReadingListItem item = itemRepository
                .findByReadingListIdAndBookId(listId, bookId)
                .orElseThrow(() -> new RuntimeException("El libro no está en esta lista"));
        itemRepository.delete(item);
    }
}