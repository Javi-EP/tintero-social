package cl.javiep.socialservice.mapper;

import cl.javiep.socialservice.dto.FollowResponseDTO;
import cl.javiep.socialservice.model.Follow;
import org.springframework.stereotype.Component;

@Component
public class SocialMapper {

    public FollowResponseDTO toResponseDTO(Follow follow) {
        FollowResponseDTO dto = new FollowResponseDTO();
        dto.setId(follow.getId());
        dto.setFollowerId(follow.getFollowerId());
        dto.setFollowedId(follow.getFollowedId());
        dto.setCreatedAt(follow.getCreatedAt());
        return dto;
    }
}