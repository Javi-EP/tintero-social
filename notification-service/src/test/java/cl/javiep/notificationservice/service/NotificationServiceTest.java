package cl.javiep.notificationservice.service;

import cl.javiep.notificationservice.client.UserClient;
import cl.javiep.notificationservice.dto.NotificationDTO;
import cl.javiep.notificationservice.mapper.NotificationMapper;
import cl.javiep.notificationservice.model.Notification;
import cl.javiep.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private NotificationMapper mapper;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void create_shouldSaveAndReturnDTO_whenSenderExists() {
        Notification entity = givenNotificationEntity(null);
        Notification saved = givenNotificationEntity(1L);
        NotificationDTO expected = givenNotificationDTO();
        UserClient.UserResponse sender = new UserClient.UserResponse();
        sender.setId(2L);
        sender.setName("Juan Pérez");

        when(repository.save(any(Notification.class))).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(expected);
        when(userClient.getUserById(2L)).thenReturn(sender);

        NotificationDTO result = notificationService.create(1L, 2L, "NEW_FOLLOWER", "Tienes un nuevo seguidor");

        assertThat(result).isEqualTo(expected);
        assertThat(result.getSenderName()).isEqualTo("Juan Pérez");
        verify(repository).save(any(Notification.class));
    }

    @Test
    void create_shouldSaveAndReturnDTO_whenNoSender() {
        Notification entity = givenNotificationEntity(null);
        Notification saved = givenNotificationEntity(1L);
        NotificationDTO expected = givenNotificationDTO();
        expected.setSenderName(null);

        when(repository.save(any(Notification.class))).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(expected);

        NotificationDTO result = notificationService.create(1L, null, "SYSTEM", "Bienvenido");

        assertThat(result).isEqualTo(expected);
        assertThat(result.getSenderName()).isNull();
        verify(userClient, never()).getUserById(any());
    }

    @Test
    void getByUserId_shouldReturnListWithSenderNames() {
        Notification notification = givenNotificationEntity(1L);
        NotificationDTO dto = givenNotificationDTO();
        UserClient.UserResponse sender = new UserClient.UserResponse();
        sender.setName("María");

        when(repository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(notification));
        when(mapper.toDTO(notification)).thenReturn(dto);
        when(userClient.getUserById(2L)).thenReturn(sender);

        List<NotificationDTO> result = notificationService.getByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getSenderName()).isEqualTo("María");
    }

    @Test
    void getByUserId_shouldHandleClientExceptionGracefully() {
        Notification notification = givenNotificationEntity(1L);
        NotificationDTO dto = givenNotificationDTO();

        when(repository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(notification));
        when(mapper.toDTO(notification)).thenReturn(dto);
        when(userClient.getUserById(2L)).thenThrow(new RuntimeException("Service unavailable"));

        List<NotificationDTO> result = notificationService.getByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getSenderName()).isNull();
    }

    @Test
    void markAsRead_shouldSetRead_whenExists() {
        Notification notification = givenNotificationEntity(1L);
        notification.setRead(false);
        Notification saved = givenNotificationEntity(1L);
        saved.setRead(true);
        NotificationDTO expected = givenNotificationDTO();
        expected.setRead(true);

        when(repository.findById(1L)).thenReturn(Optional.of(notification));
        when(repository.save(notification)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(expected);

        NotificationDTO result = notificationService.markAsRead(1L);

        assertThat(result.isRead()).isTrue();
    }

    @Test
    void markAsRead_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.markAsRead(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notification not found");
    }

    private static Notification givenNotificationEntity(Long id) {
        Notification n = new Notification();
        n.setId(id);
        n.setUserId(1L);
        n.setSenderId(2L);
        n.setType("NEW_FOLLOWER");
        n.setMessage("Tienes un nuevo seguidor");
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());
        return n;
    }

    private static NotificationDTO givenNotificationDTO() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setType("NEW_FOLLOWER");
        dto.setMessage("Tienes un nuevo seguidor");
        dto.setRead(false);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
