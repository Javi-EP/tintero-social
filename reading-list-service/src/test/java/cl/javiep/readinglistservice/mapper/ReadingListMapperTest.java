package cl.javiep.readinglistservice.mapper;

import cl.javiep.readinglistservice.dto.ItemRequestDTO;
import cl.javiep.readinglistservice.dto.ItemResponseDTO;
import cl.javiep.readinglistservice.dto.ListRequestDTO;
import cl.javiep.readinglistservice.dto.ListResponseDTO;
import cl.javiep.readinglistservice.model.ListType;
import cl.javiep.readinglistservice.model.ReadingList;
import cl.javiep.readinglistservice.model.ReadingListItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReadingListMapperTest {

    private final ReadingListMapper mapper = new ReadingListMapper();

    @Test
    void toEntity_shouldMapAllFields_whenDtoIsValid() {
        ListRequestDTO dto = new ListRequestDTO();
        dto.setUserId(1L);
        dto.setName("Favoritos");
        dto.setType(ListType.READING);
        dto.setIsPrivate(true);

        ReadingList list = mapper.toEntity(dto);

        assertThat(list.getId()).isNull();
        assertThat(list.getUserId()).isEqualTo(1L);
        assertThat(list.getName()).isEqualTo("Favoritos");
        assertThat(list.getType()).isEqualTo(ListType.READING);
        assertThat(list.getIsPrivate()).isTrue();
        assertThat(list.getCreatedAt()).isNull();
        assertThat(list.getItems()).isEmpty();
    }

    @Test
    void toResponseDTO_shouldMapAllFields_whenListHasItems() {
        ReadingListItem item = new ReadingListItem();
        item.setId(10L);
        item.setBookId(100L);
        item.setProgress(50);
        item.setAddedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        item.setFinishedAt(null);

        ReadingList list = new ReadingList();
        list.setId(1L);
        list.setUserId(1L);
        list.setName("Favoritos");
        list.setType(ListType.READING);
        list.setIsPrivate(false);
        list.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));
        list.setItems(List.of(item));

        ListResponseDTO dto = mapper.toResponseDTO(list);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Favoritos");
        assertThat(dto.getType()).isEqualTo(ListType.READING);
        assertThat(dto.getIsPrivate()).isFalse();
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getItems().getFirst().getBookId()).isEqualTo(100L);
        assertThat(dto.getItems().getFirst().getProgress()).isEqualTo(50);
    }

    @Test
    void toItemEntity_shouldMapAllFields() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setBookId(100L);
        dto.setProgress(75);

        ReadingListItem item = mapper.toItemEntity(dto);

        assertThat(item.getId()).isNull();
        assertThat(item.getBookId()).isEqualTo(100L);
        assertThat(item.getProgress()).isEqualTo(75);
    }

    @Test
    void toItemResponseDTO_shouldMapAllFields() {
        ReadingListItem item = new ReadingListItem();
        item.setId(10L);
        item.setBookId(100L);
        item.setProgress(100);
        item.setAddedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        item.setFinishedAt(LocalDateTime.of(2026, 3, 15, 15, 30));

        ItemResponseDTO dto = mapper.toItemResponseDTO(item);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getBookId()).isEqualTo(100L);
        assertThat(dto.getProgress()).isEqualTo(100);
        assertThat(dto.getAddedAt()).isEqualTo("2026-01-01T10:00:00");
        assertThat(dto.getFinishedAt()).isEqualTo("2026-03-15T15:30:00");
    }
}
