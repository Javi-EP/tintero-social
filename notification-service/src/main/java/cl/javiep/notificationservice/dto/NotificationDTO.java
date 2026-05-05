package cl.javiep.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    private String senderName;  // nombre del usuario que realizó
}