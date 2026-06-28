package cl.javiep.searchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class UserIndexDTO {
    @NotNull
    private Long userId;

    @NotBlank
    private String name;

    private String bio;
}