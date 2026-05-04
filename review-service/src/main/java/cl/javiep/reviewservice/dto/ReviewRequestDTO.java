package cl.javiep.reviewservice.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long userId;
    private Long bookId;
    private Integer rating;
    private String title;
    private String content;
}
