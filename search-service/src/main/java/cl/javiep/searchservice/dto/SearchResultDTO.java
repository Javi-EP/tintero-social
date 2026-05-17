package cl.javiep.searchservice.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
public class SearchResultDTO {
    private List<BookIndexDTO> books;
    private List<UserIndexDTO> users;
}