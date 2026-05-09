package cl.javiep.socialservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FollowResponseDTO {
    private Long id;
    private Long followerId;
    private Long followedId;
    private LocalDateTime createdAt;
}
