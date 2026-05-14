package cl.javiep.statsservice.dto;


import lombok.Data;

@Data
public class UserStatsResponseDTO {
    private Long userId;
    private Long booksRead;
    private Long pagesRead;
    private String favGenre;
}
