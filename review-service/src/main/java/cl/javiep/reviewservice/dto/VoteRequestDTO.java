package cl.javiep.reviewservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequestDTO {
    @NotNull
    private Long userId;
}