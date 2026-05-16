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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final ReadingListClient readingListClient;
    private final ReviewClient reviewClient;
    private final UserClient userClient;
    private final BookClient bookClient;
    private final StatSnapshotRepository statSnapshotRepository;

    public StatsService(
            ReadingListClient readingListClient,
            ReviewClient reviewClient,
            UserClient userClient,
            BookClient bookClient,
            StatSnapshotRepository statSnapshotRepository) {
        this.readingListClient = readingListClient;
        this.reviewClient = reviewClient;
        this.userClient = userClient;
        this.bookClient = bookClient;
        this.statSnapshotRepository = statSnapshotRepository;
    }

    public UserStatsDTO getUserStats(Long userId) {
        List<Map<String, Object>> lists = readingListClient.getListsByUser(userId);
        List<Map<String, Object>> reviews = reviewClient.getReviewsByUser(userId);

        int booksRead = countBooksRead(lists);
        int booksReading = countBooksByType(lists, "READING");
        int booksWantToRead = countBooksByType(lists, "WANT_TO_READ");
        int totalReviews = reviews.size();
        double averageRating = calculateAverageRating(reviews);
        String favoriteGenre = "N/A";

        StatSnapshot snapshot = statSnapshotRepository.findByUserId(userId)
                .orElse(new StatSnapshot());

        snapshot.setUserId(userId);
        snapshot.setBooksRead(booksRead);
        snapshot.setBooksReading(booksReading);
        snapshot.setBooksWantToRead(booksWantToRead);
        snapshot.setTotalReviews(totalReviews);
        snapshot.setAverageRating(averageRating);
        snapshot.setFavoriteGenre(favoriteGenre);
        snapshot.setLastUpdated(LocalDateTime.now());

        statSnapshotRepository.save(snapshot);

        return new UserStatsDTO(
                userId,
                booksRead,
                booksReading,
                booksWantToRead,
                totalReviews,
                averageRating,
                favoriteGenre,
                snapshot.getLastUpdated()
        );
    }

    private int countBooksRead(List<Map<String, Object>> lists) {
        int count = 0;
        for (Map<String, Object> list : lists) {
            Object itemsObj = list.get("items");
            if (itemsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObj;
                for (Map<String, Object> item : items) {
                    Object progressObj = item.get("progress");
                    if (progressObj instanceof Number) {
                        if (((Number) progressObj).intValue() == 100) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private int countBooksByType(List<Map<String, Object>> lists, String type) {
        int count = 0;
        for (Map<String, Object> list : lists) {
            Object typeObj = list.get("type");
            if (typeObj != null && typeObj.toString().equals(type)) {
                count++;
            }
        }
        return count;
    }

    private double calculateAverageRating(List<Map<String, Object>> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        int count = 0;
        for (Map<String, Object> review : reviews) {
            Object ratingObj = review.get("rating");
            if (ratingObj instanceof Number) {
                sum += ((Number) ratingObj).doubleValue();
                count++;
            }
        }
        return count > 0 ? sum / count : 0.0;
    }

    public GlobalStatsDTO getGlobalStats() {
        List<Map<String, Object>> users = userClient.getAllUsers();
        int totalUsers = users != null ? users.size() : 0;

        int totalBooks = 0;
        int totalReviews = 0;
        double averageRatingGlobal = 0.0;

        return new GlobalStatsDTO(totalUsers, totalBooks, totalReviews, averageRatingGlobal);
    }

    public List<TopBookDTO> getTopBooks() {
        List<StatSnapshot> allSnapshots = statSnapshotRepository.findAll();

        Map<Long, Integer> bookCountMap = new HashMap<>();

        for (StatSnapshot snapshot : allSnapshots) {
            List<Map<String, Object>> lists = readingListClient.getListsByUser(snapshot.getUserId());
            for (Map<String, Object> list : lists) {
                Object itemsObj = list.get("items");
                if (itemsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObj;
                    for (Map<String, Object> item : items) {
                        Object progressObj = item.get("progress");
                        Object bookIdObj = item.get("bookId");
                        if (progressObj instanceof Number && bookIdObj instanceof Number) {
                            if (((Number) progressObj).intValue() == 100) {
                                Long bookId = ((Number) bookIdObj).longValue();
                                bookCountMap.put(bookId, bookCountMap.getOrDefault(bookId, 0) + 1);
                            }
                        }
                    }
                }
            }
        }

        List<Map.Entry<Long, Integer>> topBooks = bookCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        List<TopBookDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : topBooks) {
            Long bookId = entry.getKey();
            Map<String, Object> book = bookClient.getBookById(bookId);
            String title = book != null && book.get("title") != null
                    ? book.get("title").toString() : "Unknown";
            String author = book != null && book.get("author") != null
                    ? book.get("author").toString() : "Unknown";
            result.add(new TopBookDTO(bookId, title, author, entry.getValue()));
        }

        return result;
    }
}