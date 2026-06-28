package cl.javiep.readinglistservice.service;

import cl.javiep.readinglistservice.client.BookClient;
import cl.javiep.readinglistservice.client.UserClient;
import cl.javiep.readinglistservice.dto.ItemRequestDTO;
import cl.javiep.readinglistservice.dto.ListRequestDTO;
import cl.javiep.readinglistservice.dto.ListResponseDTO;
import cl.javiep.readinglistservice.mapper.ReadingListMapper;
import cl.javiep.readinglistservice.model.ListType;
import cl.javiep.readinglistservice.model.ReadingList;
import cl.javiep.readinglistservice.model.ReadingListItem;
import cl.javiep.readinglistservice.repository.ReadingListItemRepository;
import cl.javiep.readinglistservice.repository.ReadingListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingListServiceTest {

    @Mock
    private ReadingListRepository listRepository;

    @Mock
    private ReadingListItemRepository itemRepository;

    @Mock
    private ReadingListMapper mapper;

    @Mock
    private BookClient bookClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ReadingListService service;

    @Test
    void createList_shouldSave_whenUserExists() {
        ListRequestDTO dto = givenListRequestDTO();
        ReadingList entity = new ReadingList();
        ReadingList saved = new ReadingList();
        ListResponseDTO expected = new ListResponseDTO();

        when(userClient.userExists(dto.getUserId())).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(listRepository.save(entity)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(expected);

        ListResponseDTO result = service.createList(dto);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void createList_shouldThrow_whenUserDoesNotExist() {
        ListRequestDTO dto = givenListRequestDTO();
        when(userClient.userExists(dto.getUserId())).thenReturn(false);

        assertThatThrownBy(() -> service.createList(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(listRepository, never()).save(any());
    }

    @Test
    void getListsByUser_shouldReturnLists() {
        ReadingList list = new ReadingList();
        ListResponseDTO dto = new ListResponseDTO();
        when(listRepository.findByUserId(1L)).thenReturn(List.of(list));
        when(mapper.toResponseDTO(list)).thenReturn(dto);

        List<ListResponseDTO> result = service.getListsByUser(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getListById_shouldReturnList_whenExists() {
        ReadingList list = new ReadingList();
        ListResponseDTO dto = new ListResponseDTO();
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(mapper.toResponseDTO(list)).thenReturn(dto);

        ListResponseDTO result = service.getListById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getListById_shouldThrow_whenNotFound() {
        when(listRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getListById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Lista no encontrada");
    }

    @Test
    void deleteList_shouldRemove_whenExists() {
        ReadingList list = new ReadingList();
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));

        service.deleteList(1L);

        verify(listRepository).deleteById(1L);
    }

    @Test
    void deleteList_shouldThrow_whenNotFound() {
        when(listRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteList(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Lista no encontrada");

        verify(listRepository, never()).deleteById(any());
    }

    @Test
    void addBook_shouldAdd_whenBookNotInList() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setBookId(100L);

        ReadingList list = new ReadingList();
        list.setId(1L);
        list.setItems(new java.util.ArrayList<>());

        ReadingListItem item = new ReadingListItem();
        ListResponseDTO expected = new ListResponseDTO();

        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(bookClient.bookExists(100L)).thenReturn(true);
        when(itemRepository.existsByReadingListIdAndBookId(1L, 100L)).thenReturn(false);
        when(mapper.toItemEntity(dto)).thenReturn(item);
        when(listRepository.save(list)).thenReturn(list);
        when(mapper.toResponseDTO(list)).thenReturn(expected);

        ListResponseDTO result = service.addBook(1L, dto);

        assertThat(result).isEqualTo(expected);
        assertThat(list.getItems()).contains(item);
    }

    @Test
    void addBook_shouldThrow_whenBookDoesNotExist() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setBookId(100L);

        ReadingList list = new ReadingList();
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(bookClient.bookExists(100L)).thenReturn(false);

        assertThatThrownBy(() -> service.addBook(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Libro no encontrado");
    }

    @Test
    void addBook_shouldThrow_whenBookAlreadyInList() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setBookId(100L);

        ReadingList list = new ReadingList();
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(bookClient.bookExists(100L)).thenReturn(true);
        when(itemRepository.existsByReadingListIdAndBookId(1L, 100L)).thenReturn(true);

        assertThatThrownBy(() -> service.addBook(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya está en esta lista");
    }

    @Test
    void updateProgress_shouldSetProgress() {
        ReadingListItem item = new ReadingListItem();
        ReadingList list = new ReadingList();
        list.setId(1L);

        when(itemRepository.findByReadingListIdAndBookId(1L, 100L))
                .thenReturn(Optional.of(item));
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));

        ListResponseDTO expected = new ListResponseDTO();
        when(mapper.toResponseDTO(list)).thenReturn(expected);

        ListResponseDTO result = service.updateProgress(1L, 100L, 50);

        assertThat(result).isEqualTo(expected);
        assertThat(item.getProgress()).isEqualTo(50);
        assertThat(item.getFinishedAt()).isNull();
        verify(itemRepository).save(item);
    }

    @Test
    void updateProgress_shouldSetFinishedAt_whenProgressIs100() {
        ReadingListItem item = new ReadingListItem();
        ReadingList list = new ReadingList();
        list.setId(1L);

        when(itemRepository.findByReadingListIdAndBookId(1L, 100L))
                .thenReturn(Optional.of(item));
        when(listRepository.findById(1L)).thenReturn(Optional.of(list));

        ListResponseDTO expected = new ListResponseDTO();
        when(mapper.toResponseDTO(list)).thenReturn(expected);

        ListResponseDTO result = service.updateProgress(1L, 100L, 100);

        assertThat(result).isEqualTo(expected);
        assertThat(item.getProgress()).isEqualTo(100);
        assertThat(item.getFinishedAt()).isNotNull();
    }

    @Test
    void removeBook_shouldDelete_whenItemExists() {
        ReadingListItem item = new ReadingListItem();
        when(itemRepository.findByReadingListIdAndBookId(1L, 100L))
                .thenReturn(Optional.of(item));

        service.removeBook(1L, 100L);

        verify(itemRepository).delete(item);
    }

    @Test
    void removeBook_shouldThrow_whenItemNotFound() {
        when(itemRepository.findByReadingListIdAndBookId(99L, 100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeBook(99L, 100L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("libro no está en esta lista");

        verify(itemRepository, never()).delete(any());
    }

    private static ListRequestDTO givenListRequestDTO() {
        ListRequestDTO dto = new ListRequestDTO();
        dto.setUserId(1L);
        dto.setName("Favoritos");
        dto.setType(ListType.READING);
        dto.setIsPrivate(false);
        return dto;
    }
}
