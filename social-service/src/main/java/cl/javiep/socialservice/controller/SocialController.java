package cl.javiep.socialservice.controller;

import cl.javiep.socialservice.dto.FeedItemDTO;
import cl.javiep.socialservice.dto.FollowResponseDTO;
import cl.javiep.socialservice.service.SocialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    // POST /api/social/follow — seguir a un usuario
    @PostMapping("/follow")
    public ResponseEntity<FollowResponseDTO> follow(
            @RequestParam Long followerId,
            @RequestParam Long followedId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(socialService.follow(followerId, followedId));
    }

    // DELETE /api/social/follow — dejar de seguir
    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollow(
            @RequestParam Long followerId,
            @RequestParam Long followedId) {
        socialService.unfollow(followerId, followedId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/social/followers/{userId} — seguidores de un usuario
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowResponseDTO>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowers(userId));
    }

    // GET /api/social/following/{userId} — usuarios que sigue
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowResponseDTO>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowing(userId));
    }

    // GET /api/social/feed/{userId} — feed de actividad
    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<FeedItemDTO>> getFeed(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFeed(userId));
    }

    // GET /api/social/stats/{userId} — estadísticas de seguimiento
    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getStats(userId));
    }
}