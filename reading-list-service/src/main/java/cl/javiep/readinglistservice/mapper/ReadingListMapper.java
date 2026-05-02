package cl.javiep.readinglistservice.mapper;

import cl.javiep.readinglistservice.dto.*;
import cl.javiep.readinglistservice.model.ReadingList;
import cl.javiep.readinglistservice.model.ReadingListItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReadingListMapper {

    public ReadingList toEntity(ListRequestDTO dto) {
        ReadingList list = new ReadingList();
        list.setUserId(dto.getUserId());
        list.setName(dto.getName());
        list.setType(dto.getType());
        list.setIsPrivate(dto.getIsPrivate());
        return list;
    }

    public ListResponseDTO toResponseDTO(ReadingList list) {
        ListResponseDTO dto = new ListResponseDTO();
        dto.setId(list.getId());
        dto.setUserId(list.getUserId());
        dto.setName(list.getName());
        dto.setType(list.getType());
        dto.setIsPrivate(list.getIsPrivate());
        dto.setCreatedAt(list.getCreatedAt());
        dto.setItems(list.getItems().stream()
                .map(this::toItemResponseDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public ReadingListItem toItemEntity(ItemRequestDTO dto) {
        ReadingListItem item = new ReadingListItem();
        item.setBookId(dto.getBookId());
        item.setProgress(dto.getProgress());
        return item;
    }

    public ItemResponseDTO toItemResponseDTO(ReadingListItem item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setBookId(item.getBookId());
        dto.setProgress(item.getProgress());
        dto.setAddedAt(item.getAddedAt());
        dto.setFinishedAt(item.getFinishedAt());
        return dto;
    }
}