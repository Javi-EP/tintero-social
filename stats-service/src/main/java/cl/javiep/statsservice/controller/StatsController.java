package cl.javiep.statsservice.controller;

import cl.javiep.statsservice.dto.GlobalStatsDTO;
import cl.javiep.statsservice.dto.TopBookDTO;
import cl.javiep.statsservice.dto.UserStatsDTO;
import cl.javiep.statsservice.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.getUserStats(userId));
    }

    @GetMapping("/global")
    public ResponseEntity<GlobalStatsDTO> getGlobalStats() {
        return ResponseEntity.ok(statsService.getGlobalStats());
    }

    @GetMapping("/books/top")
    public ResponseEntity<List<TopBookDTO>> getTopBooks() {
        return ResponseEntity.ok(statsService.getTopBooks());
    }

    @PostMapping("/refresh/{userId}")
    public ResponseEntity<UserStatsDTO> refreshUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.getUserStats(userId));
    }
}