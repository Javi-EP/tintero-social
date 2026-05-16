package cl.javiep.statsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopBookDTO {
    private Long bookId;
    private String title;
    private String author;
    private Integer timesRead;
}