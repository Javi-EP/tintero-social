package cl.javiep.searchservice.dto;

import lombok.*;

@Data
@Builder
public class UserIndexDTO {
    private Long userId;
    private String name;
    private String bio;
}