package cl.javiep.searchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class BookIndexDTO {
    @NotNull
    private Long bookId;

    @NotBlank
    private String title;

    private String author;
    private String genre;
    private String tags;
    private Double rating;
}