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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SocialService {

    private final FollowRepository followRepository;
    private final SocialMapper mapper;
    private final UserClient userClient;
    private final ReviewClient reviewClient;
    private final ReadingListClient readingListClient;

    public SocialService(FollowRepository followRepository, SocialMapper mapper,
                         UserClient userClient, ReviewClient reviewClient,
                         ReadingListClient readingListClient) {
        this.followRepository = followRepository;
        this.mapper = mapper;
        this.userClient = userClient;
        this.reviewClient = reviewClient;
        this.readingListClient = readingListClient;
    }

    // Seguir a un usuario
    public FollowResponseDTO follow(Long followerId, Long followedId) {
        log.info("Usuario {} sigue a usuario {}", followerId, followedId);

        // No puedes seguirte a ti mismo
        if (followerId.equals(followedId)) {
            log.warn("Intento de auto-seguimiento del usuario {}", followerId);
            throw new RuntimeException("No puedes seguirte a ti mismo");
        }

        // Verificar que ambos usuarios existen
        if (!userClient.userExists(followerId)) {
            log.warn("Seguidor no encontrado con ID: {}", followerId);
            throw new RuntimeException("Seguidor no encontrado con ID: " + followerId);
        }
        if (!userClient.userExists(followedId)) {
            log.warn("Usuario a seguir no encontrado con ID: {}", followedId);
            throw new RuntimeException("Usuario a seguir no encontrado con ID: " + followedId);
        }

        // Verificar que no sigue ya a ese usuario
        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            log.warn("El usuario {} ya sigue a {}", followerId, followedId);
            throw new RuntimeException("Ya sigues a este usuario");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);

        Follow saved = followRepository.save(follow);
        log.info("Follow registrado: {} -> {} (ID: {})", followerId, followedId, saved.getId());
        return mapper.toResponseDTO(saved);
    }

    // Dejar de seguir
    public void unfollow(Long followerId, Long followedId) {
        log.info("Usuario {} deja de seguir a {}", followerId, followedId);
        Follow follow = followRepository
                .findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> {
                    log.warn("El usuario {} no sigue a {}", followerId, followedId);
                    return new RuntimeException("No sigues a este usuario");
                });
        followRepository.delete(follow);
        log.info("Unfollow completado: {} -> {}", followerId, followedId);
    }

    // Listar seguidores de un usuario
    public List<FollowResponseDTO> getFollowers(Long userId) {
        return followRepository.findByFollowedId(userId)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Listar usuarios que sigue
    public List<FollowResponseDTO> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Feed de actividad — reúne eventos de los usuarios seguidos
    public List<FeedItemDTO> getFeed(Long userId) {
        log.info("Generando feed para usuario {}", userId);
        List<FeedItemDTO> feed = new ArrayList<>();

        // Obtener IDs de usuarios que sigue
        List<Long> followingIds = followRepository.findByFollowerId(userId)
                .stream()
                .map(Follow::getFollowedId)
                .collect(Collectors.toList());

        if (followingIds.isEmpty()) {
            log.info("Feed vacio para usuario {} (no sigue a nadie)", userId);
            return feed;
        }

        for (Long followedId : followingIds) {

            // 1. Reseñas recientes del usuario seguido
            List<ReviewDTO> reviews = reviewClient.getReviewsByUser(followedId);
            for (ReviewDTO review : reviews) {
                FeedItemDTO item = new FeedItemDTO(
                        "REVIEW",
                        followedId,
                        "Usuario " + followedId + " escribió una reseña del libro #"
                                + review.getBookId() + " — " + review.getRating() + "⭐",
                        review,
                        review.getCreatedAt()
                );
                feed.add(item);
            }

            // 2. Listas de lectura del usuario seguido
            List<Map<String, Object>> lists = readingListClient.getListsByUser(followedId);
            for (Map<String, Object> list : lists) {
                String type = (String) list.get("type");
                String name = (String) list.get("name");
                String emoji = switch (type != null ? type : "") {
                    case "READING"      -> "📖";
                    case "READ"         -> "✅";
                    case "WANT_TO_READ" -> "📋";
                    default             -> "📚";
                };
                FeedItemDTO item = new FeedItemDTO(
                        "LIST",
                        followedId,
                        "Usuario " + followedId + " tiene una lista " + emoji + " \"" + name + "\"",
                        list,
                        LocalDateTime.now()
                );
                feed.add(item);
            }
        }

        // Ordenar por timestamp más reciente primero
        feed.sort(Comparator.comparing(FeedItemDTO::getTimestamp).reversed());

        log.info("Feed generado para usuario {} con {} items", userId, feed.size());
        return feed;
    }

    // Stats de un usuario
    public Map<String, Long> getStats(Long userId) {
        return Map.of(
                "following", followRepository.countByFollowerId(userId),
                "followers", followRepository.countByFollowedId(userId)
        );
    }
}