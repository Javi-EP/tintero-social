package cl.javiep.socialservice.service;

import cl.javiep.socialservice.client.ReadingListClient;
import cl.javiep.socialservice.client.ReviewClient;
import cl.javiep.socialservice.client.UserClient;
import cl.javiep.socialservice.dto.FeedItemDTO;
import cl.javiep.socialservice.dto.FollowResponseDTO;
import cl.javiep.socialservice.dto.ReviewDTO;
import cl.javiep.socialservice.mapper.SocialMapper;
import cl.javiep.socialservice.model.Follow;
import cl.javiep.socialservice.repository.FollowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocialServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private SocialMapper mapper;

    @Mock
    private UserClient userClient;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private ReadingListClient readingListClient;

    @InjectMocks
    private SocialService socialService;

    @Test
    void follow_shouldSave_whenAllChecksPass() {
        Follow follow = new Follow();
        Follow saved = new Follow();
        FollowResponseDTO expected = new FollowResponseDTO();

        when(userClient.userExists(1L)).thenReturn(true);
        when(userClient.userExists(2L)).thenReturn(true);
        when(followRepository.existsByFollowerIdAndFollowedId(1L, 2L)).thenReturn(false);
        when(followRepository.save(any(Follow.class))).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(expected);

        FollowResponseDTO result = socialService.follow(1L, 2L);

        assertThat(result).isEqualTo(expected);
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void follow_shouldThrow_whenFollowingSelf() {
        assertThatThrownBy(() -> socialService.follow(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("seguirte a ti mismo");

        verify(followRepository, never()).save(any());
    }

    @Test
    void follow_shouldThrow_whenFollowerDoesNotExist() {
        when(userClient.userExists(1L)).thenReturn(false);

        assertThatThrownBy(() -> socialService.follow(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Seguidor no encontrado");

        verify(followRepository, never()).save(any());
    }

    @Test
    void follow_shouldThrow_whenFollowedDoesNotExist() {
        when(userClient.userExists(1L)).thenReturn(true);
        when(userClient.userExists(2L)).thenReturn(false);

        assertThatThrownBy(() -> socialService.follow(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario a seguir no encontrado");

        verify(followRepository, never()).save(any());
    }

    @Test
    void follow_shouldThrow_whenAlreadyFollowing() {
        when(userClient.userExists(1L)).thenReturn(true);
        when(userClient.userExists(2L)).thenReturn(true);
        when(followRepository.existsByFollowerIdAndFollowedId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> socialService.follow(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya sigues");

        verify(followRepository, never()).save(any());
    }

    @Test
    void unfollow_shouldRemove_whenFollowExists() {
        Follow follow = new Follow();
        when(followRepository.findByFollowerIdAndFollowedId(1L, 2L))
                .thenReturn(Optional.of(follow));

        socialService.unfollow(1L, 2L);

        verify(followRepository).delete(follow);
    }

    @Test
    void unfollow_shouldThrow_whenNotFollowing() {
        when(followRepository.findByFollowerIdAndFollowedId(1L, 99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> socialService.unfollow(1L, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No sigues");

        verify(followRepository, never()).delete(any());
    }

    @Test
    void getFollowers_shouldReturnList() {
        Follow follow = new Follow();
        FollowResponseDTO dto = new FollowResponseDTO();
        when(followRepository.findByFollowedId(2L)).thenReturn(List.of(follow));
        when(mapper.toResponseDTO(follow)).thenReturn(dto);

        List<FollowResponseDTO> result = socialService.getFollowers(2L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getFollowing_shouldReturnList() {
        Follow follow = new Follow();
        FollowResponseDTO dto = new FollowResponseDTO();
        when(followRepository.findByFollowerId(1L)).thenReturn(List.of(follow));
        when(mapper.toResponseDTO(follow)).thenReturn(dto);

        List<FollowResponseDTO> result = socialService.getFollowing(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getStats_shouldReturnCounts() {
        when(followRepository.countByFollowerId(1L)).thenReturn(5L);
        when(followRepository.countByFollowedId(1L)).thenReturn(3L);

        Map<String, Long> result = socialService.getStats(1L);

        assertThat(result).containsEntry("following", 5L);
        assertThat(result).containsEntry("followers", 3L);
    }

    @Test
    void getFeed_shouldReturnEmpty_whenNotFollowingAnyone() {
        when(followRepository.findByFollowerId(1L)).thenReturn(List.of());

        List<FeedItemDTO> result = socialService.getFeed(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getFeed_shouldAggregateReviewsAndLists_whenFollowingUsers() {
        Follow follow = new Follow();
        follow.setFollowedId(2L);
        follow.setFollowerId(1L);

        ReviewDTO review = new ReviewDTO();
        review.setBookId(100L);
        review.setRating(4);
        review.setCreatedAt(LocalDateTime.now());

        Map<String, Object> list = Map.of(
                "type", "READING",
                "name", "Favoritos"
        );

        when(followRepository.findByFollowerId(1L)).thenReturn(List.of(follow));
        when(reviewClient.getReviewsByUser(2L)).thenReturn(List.of(review));
        when(readingListClient.getListsByUser(2L)).thenReturn(List.of(list));

        List<FeedItemDTO> result = socialService.getFeed(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getType()).isIn("REVIEW", "LIST");
    }
}
