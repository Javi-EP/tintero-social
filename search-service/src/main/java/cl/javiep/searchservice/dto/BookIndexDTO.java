package cl.javiep.searchservice.dto;

import lombok.*;

@Data
@Builder
public class BookIndexDTO {
    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private String tags;
    private Double rating;
}