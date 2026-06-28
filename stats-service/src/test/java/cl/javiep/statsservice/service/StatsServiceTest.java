package cl.javiep.statsservice.service;

import cl.javiep.statsservice.client.BookClient;
import cl.javiep.statsservice.client.ReadingListClient;
import cl.javiep.statsservice.client.ReviewClient;
import cl.javiep.statsservice.client.UserClient;
import cl.javiep.statsservice.dto.GlobalStatsDTO;
import cl.javiep.statsservice.dto.TopBookDTO;
import cl.javiep.statsservice.dto.UserStatsDTO;
import cl.javiep.statsservice.model.StatSnapshot;
import cl.javiep.statsservice.repository.StatSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private ReadingListClient readingListClient;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private UserClient userClient;

    @Mock
    private BookClient bookClient;

    @Mock
    private StatSnapshotRepository statSnapshotRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void getUserStats_shouldCreateSnapshotWithComputedStats() {
        List<Map<String, Object>> lists = List.of(
                Map.of("type", "READING",
                        "items", List.of(
                                Map.of("bookId", 1, "progress", 100),
                                Map.of("bookId", 2, "progress", 50)
                        ))
        );

        List<Map<String, Object>> reviews = List.of(
                Map.of("rating", 4),
                Map.of("rating", 5)
        );

        when(readingListClient.getListsByUser(1L)).thenReturn(lists);
        when(reviewClient.getReviewsByUser(1L)).thenReturn(reviews);
        when(statSnapshotRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(statSnapshotRepository.save(any(StatSnapshot.class)))
                .thenAnswer(i -> i.getArgument(0));

        UserStatsDTO result = statsService.getUserStats(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBooksRead()).isEqualTo(1);
        assertThat(result.getBooksReading()).isEqualTo(1);
        assertThat(result.getTotalReviews()).isEqualTo(2);
        assertThat(result.getAverageRating()).isEqualTo(4.5);
    }

    @Test
    void getUserStats_shouldUpdateExistingSnapshot() {
        List<Map<String, Object>> lists = List.of();
        List<Map<String, Object>> reviews = List.of();
        StatSnapshot existing = new StatSnapshot();
        existing.setId(1L);
        existing.setUserId(1L);

        when(readingListClient.getListsByUser(1L)).thenReturn(lists);
        when(reviewClient.getReviewsByUser(1L)).thenReturn(reviews);
        when(statSnapshotRepository.findByUserId(1L)).thenReturn(Optional.of(existing));
        when(statSnapshotRepository.save(any(StatSnapshot.class)))
                .thenAnswer(i -> i.getArgument(0));

        UserStatsDTO result = statsService.getUserStats(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBooksRead()).isEqualTo(0);
        assertThat(result.getAverageRating()).isEqualTo(0.0);
    }

    @Test
    void getGlobalStats_shouldReturnStats() {
        when(userClient.getAllUsers()).thenReturn(List.of(Map.of("id", 1), Map.of("id", 2)));

        GlobalStatsDTO result = statsService.getGlobalStats();

        assertThat(result.getTotalUsers()).isEqualTo(2);
        assertThat(result.getTotalBooks()).isEqualTo(0);
        assertThat(result.getTotalReviews()).isEqualTo(0);
    }

    @Test
    void getGlobalStats_shouldHandleNullUsers() {
        when(userClient.getAllUsers()).thenReturn(null);

        GlobalStatsDTO result = statsService.getGlobalStats();

        assertThat(result.getTotalUsers()).isEqualTo(0);
    }

    @Test
    void getTopBooks_shouldReturnTopBooks() {
        StatSnapshot snapshot = new StatSnapshot();
        snapshot.setUserId(1L);

        when(statSnapshotRepository.findAll()).thenReturn(List.of(snapshot));
        when(readingListClient.getListsByUser(1L)).thenReturn(List.of(
                Map.of("items", List.of(
                        Map.of("bookId", 10, "progress", 100),
                        Map.of("bookId", 20, "progress", 100),
                        Map.of("bookId", 10, "progress", 100)
                ))
        ));
        when(bookClient.getBookById(10L)).thenReturn(Map.of("title", "Libro A", "author", "Autor A"));
        when(bookClient.getBookById(20L)).thenReturn(Map.of("title", "Libro B", "author", "Autor B"));

        List<TopBookDTO> result = statsService.getTopBooks();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getBookId()).isEqualTo(10L);
        assertThat(result.getFirst().getTimesRead()).isEqualTo(2);
        assertThat(result.get(1).getBookId()).isEqualTo(20L);
        assertThat(result.get(1).getTimesRead()).isEqualTo(1);
    }

    @Test
    void getTopBooks_shouldReturnEmpty_whenNoData() {
        when(statSnapshotRepository.findAll()).thenReturn(List.of());

        List<TopBookDTO> result = statsService.getTopBooks();

        assertThat(result).isEmpty();
    }
}
