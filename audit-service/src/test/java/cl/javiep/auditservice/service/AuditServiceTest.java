package cl.javiep.auditservice.service;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.mapper.AuditMapper;
import cl.javiep.auditservice.model.AuditEvent;
import cl.javiep.auditservice.model.EventType;
import cl.javiep.auditservice.repository.AuditEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditService auditService;

    @Test
    void registerEvent_shouldSaveAndReturnDTO() {
        AuditEventRequestDTO dto = givenRequestDTO();
        AuditEvent entity = givenEntity(null);
        AuditEvent saved = givenEntity(1L);
        AuditEventResponseDTO expected = givenResponseDTO();

        when(auditMapper.toEntity(dto)).thenReturn(entity);
        when(auditEventRepository.save(entity)).thenReturn(saved);
        when(auditMapper.toResponseDTO(saved)).thenReturn(expected);

        AuditEventResponseDTO result = auditService.registerEvent(dto);

        assertThat(result).isEqualTo(expected);
        verify(auditEventRepository).save(entity);
    }

    @Test
    void getAllEvents_shouldReturnList() {
        AuditEvent event = givenEntity(1L);
        AuditEventResponseDTO dto = givenResponseDTO();

        when(auditEventRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(event));
        when(auditMapper.toResponseDTO(event)).thenReturn(dto);

        List<AuditEventResponseDTO> result = auditService.getAllEvents();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(dto);
    }

    @Test
    void getAllEvents_shouldReturnEmpty_whenNone() {
        when(auditEventRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of());

        List<AuditEventResponseDTO> result = auditService.getAllEvents();

        assertThat(result).isEmpty();
    }

    @Test
    void getEventsByUser_shouldReturnFilteredList() {
        AuditEvent event = givenEntity(1L);
        AuditEventResponseDTO dto = givenResponseDTO();

        when(auditEventRepository.findByUserId(1L)).thenReturn(List.of(event));
        when(auditMapper.toResponseDTO(event)).thenReturn(dto);

        List<AuditEventResponseDTO> result = auditService.getEventsByUser(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getEventsByType_shouldReturnFilteredList() {
        AuditEvent event = givenEntity(1L);
        AuditEventResponseDTO dto = givenResponseDTO();

        when(auditEventRepository.findByEventType(EventType.USER_REGISTERED))
                .thenReturn(List.of(event));
        when(auditMapper.toResponseDTO(event)).thenReturn(dto);

        List<AuditEventResponseDTO> result = auditService.getEventsByType("USER_REGISTERED");

        assertThat(result).hasSize(1);
    }

    @Test
    void getEventsByType_shouldThrow_whenInvalidType() {
        assertThatThrownBy(() -> auditService.getEventsByType("INVALID_TYPE"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tipo de evento inválido");
    }

    @Test
    void getEventsByResource_shouldReturnFilteredList() {
        AuditEvent event = givenEntity(1L);
        AuditEventResponseDTO dto = givenResponseDTO();

        when(auditEventRepository.findByResourceIdAndResourceType(10L, "BOOK"))
                .thenReturn(List.of(event));
        when(auditMapper.toResponseDTO(event)).thenReturn(dto);

        List<AuditEventResponseDTO> result = auditService.getEventsByResource(10L, "BOOK");

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteEvent_shouldRemove_whenExists() {
        when(auditEventRepository.existsById(1L)).thenReturn(true);

        auditService.deleteEvent(1L);

        verify(auditEventRepository).deleteById(1L);
    }

    @Test
    void deleteEvent_shouldThrow_whenNotFound() {
        when(auditEventRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> auditService.deleteEvent(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Evento no encontrado");

        verify(auditEventRepository, never()).deleteById(any());
    }

    private static AuditEventRequestDTO givenRequestDTO() {
        AuditEventRequestDTO dto = new AuditEventRequestDTO();
        dto.setUserId(1L);
        dto.setEventType(EventType.USER_REGISTERED);
        dto.setDescription("Usuario registrado exitosamente");
        dto.setResourceId(10L);
        dto.setResourceType("USER");
        return dto;
    }

    private static AuditEvent givenEntity(Long id) {
        AuditEvent event = new AuditEvent();
        event.setId(id);
        event.setUserId(1L);
        event.setEventType(EventType.USER_REGISTERED);
        event.setDescription("Usuario registrado exitosamente");
        event.setResourceId(10L);
        event.setResourceType("USER");
        event.setCreatedAt(LocalDateTime.now());
        return event;
    }

    private static AuditEventResponseDTO givenResponseDTO() {
        AuditEventResponseDTO dto = new AuditEventResponseDTO();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setEventType(EventType.USER_REGISTERED);
        dto.setDescription("Usuario registrado exitosamente");
        dto.setResourceId(10L);
        dto.setResourceType("USER");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
