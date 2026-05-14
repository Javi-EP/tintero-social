package cl.javiep.statsservice.controller;

import cl.javiep.statsservice.dto.UserStatsResponseDTO;
import cl.javiep.statsservice.service.UserStatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class UserStatController {
    private final UserStatService service;

    public UserStatController(UserStatService service){
        this.service = service;
    }

    //Metodo para acceder a todos los stats del usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserStatsResponseDTO>>getByUser(@PathVariable Long userId){
        return ResponseEntity.ok(service.getStatsByUser(userId));
    }
}
