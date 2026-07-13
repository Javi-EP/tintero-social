package cl.javiep.notificationservice.service;

import cl.javiep.notificationservice.client.UserClient;
import cl.javiep.notificationservice.dto.NotificationDTO;
import cl.javiep.notificationservice.mapper.NotificationMapper;
import cl.javiep.notificationservice.model.Notification;
import cl.javiep.notificationservice.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final UserClient userClient;

    public NotificationService(NotificationRepository repository, NotificationMapper mapper, UserClient userClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.userClient = userClient;
    }

    public NotificationDTO create(Long userId, Long senderId, String type, String message) {
        log.info("Creando notificacion para usuario {} tipo={}", userId, type);
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);

        Notification saved = repository.save(notification);
        log.info("Notificacion guardada con ID: {}", saved.getId());
        
        NotificationDTO dto = mapper.toDTO(saved);
        
        if (senderId != null) {
            UserClient.UserResponse sender = userClient.getUserById(senderId);
            if (sender != null && sender.getName() != null) {
                dto.setSenderName(sender.getName());
            }
        }
        
        return dto;
    }

    public List<NotificationDTO> getByUserId(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByCreatedAtDesc(userId);
        
        return notifications.stream().map(notification -> {
            NotificationDTO dto = mapper.toDTO(notification);
            if (notification.getSenderId() != null) {
                try {
                    UserClient.UserResponse sender = userClient.getUserById(notification.getSenderId());
                    if (sender != null && sender.getName() != null) {
                        dto.setSenderName(sender.getName());
                    }
                } catch (Exception e) {
                    log.warn("Error al obtener senderName para notificacion {}: {}", notification.getId(), e.getMessage());
                }
            }
            return dto;
        }).toList();
    }

    public NotificationDTO markAsRead(Long id) {
        log.info("Marcando notificacion {} como leida", id);
        Notification notification = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificacion no encontrada con ID: {}", id);
                    return new RuntimeException("Notification not found");
                });
        notification.setRead(true);
        Notification saved = repository.save(notification);
        log.info("Notificacion {} marcada como leida", saved.getId());
        return mapper.toDTO(saved);
    }
}