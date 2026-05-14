package cl.javiep.statsservice.dto;

import lombok.Data;

@Data
public class UserStatsRequestDTO {
    private Long userId;
    private Long booksRead;
    private Long pagesRead;

}
