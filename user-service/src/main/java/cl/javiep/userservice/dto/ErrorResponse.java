package cl.javiep.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta uniforme de error")
public class ErrorResponse {

    @Schema(example = "2026-06-04T10:30:00")
    private LocalDateTime timestamp;

    @Schema(example = "404")
    private int status;

    @Schema(example = "Usuario no encontrado")
    private String message;

    @Schema(example = "/api/users/{id}")
    private String path;
}