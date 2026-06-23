package cl.javiep.socialservice.controller;

import cl.javiep.socialservice.dto.FeedItemDTO;
import cl.javiep.socialservice.dto.FollowResponseDTO;
import cl.javiep.socialservice.service.SocialLinkAssembler;
import cl.javiep.socialservice.service.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
@Tag(name = "Social", description = "Operaciones de red social (seguir usuarios, feed de actividad)")
public class SocialController {

    private final SocialService socialService;
    private final SocialLinkAssembler linkAssembler;

    public SocialController(SocialService socialService, SocialLinkAssembler linkAssembler) {
        this.socialService = socialService;
        this.linkAssembler = linkAssembler;
    }

    @Operation(summary = "Seguir usuario", description = "Sigue a un usuario. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ahora sigues a este usuario"),
            @ApiResponse(responseCode = "409", description = "Ya sigues a este usuario")
    })
    @PostMapping("/follow")
    public ResponseEntity<EntityModel<FollowResponseDTO>> follow(
            @Parameter(description = "ID del seguidor", example = "1")
            @RequestParam Long followerId,
            @Parameter(description = "ID del usuario a seguir", example = "2")
            @RequestParam Long followedId) {
        FollowResponseDTO follow = socialService.follow(followerId, followedId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkAssembler.toModel(follow));
    }

    @Operation(summary = "Dejar de seguir", description = "Deja de seguir a un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Has dejado de seguir a este usuario"),
            @ApiResponse(responseCode = "404", description = "No sigues a este usuario")
    })
    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollow(
            @Parameter(description = "ID del seguidor", example = "1")
            @RequestParam Long followerId,
            @Parameter(description = "ID del usuario a dejar de seguir", example = "2")
            @RequestParam Long followedId) {
        socialService.unfollow(followerId, followedId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener seguidores", description = "Obtiene los seguidores de un usuario. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "200", description = "Seguidores obtenidos correctamente")
    @GetMapping("/followers/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<FollowResponseDTO>>> getFollowers(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        List<EntityModel<FollowResponseDTO>> follows = socialService.getFollowers(userId).stream()
                .map(linkAssembler::toModel)
                .toList();
        return ResponseEntity.ok(linkAssembler.toCollectionModel(follows));
    }

    @Operation(summary = "Obtener seguidos", description = "Obtiene los usuarios que sigue un usuario. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
    @GetMapping("/following/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<FollowResponseDTO>>> getFollowing(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        List<EntityModel<FollowResponseDTO>> follows = socialService.getFollowing(userId).stream()
                .map(linkAssembler::toModel)
                .toList();
        return ResponseEntity.ok(linkAssembler.toCollectionModel(follows));
    }

    @Operation(summary = "Obtener feed", description = "Obtiene el feed de actividad de un usuario")
    @ApiResponse(responseCode = "200", description = "Feed obtenido correctamente")
    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<FeedItemDTO>> getFeed(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFeed(userId));
    }

    @Operation(summary = "Obtener estadisticas", description = "Obtiene estadisticas de seguimiento de un usuario")
    @ApiResponse(responseCode = "200", description = "Estadisticas obtenidas correctamente")
    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Long>> getStats(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getStats(userId));
    }
}
