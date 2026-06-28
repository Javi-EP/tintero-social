package cl.javiep.socialservice.mapper;

import cl.javiep.socialservice.dto.FollowResponseDTO;
import cl.javiep.socialservice.model.Follow;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SocialMapperTest {

    private final SocialMapper mapper = new SocialMapper();

    @Test
    void toResponseDTO_shouldMapAllFields() {
        Follow follow = new Follow();
        follow.setId(1L);
        follow.setFollowerId(10L);
        follow.setFollowedId(20L);
        follow.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));

        FollowResponseDTO dto = mapper.toResponseDTO(follow);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFollowerId()).isEqualTo(10L);
        assertThat(dto.getFollowedId()).isEqualTo(20L);
        assertThat(dto.getCreatedAt()).isEqualTo("2026-01-15T10:30:00");
    }
}
