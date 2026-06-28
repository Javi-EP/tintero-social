package cl.javiep.auditservice.mapper;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.model.AuditEvent;
import cl.javiep.auditservice.model.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AuditMapperTest {

    private final AuditMapper mapper = new AuditMapper();

    @Test
    void toEntity_shouldMapAllFields() {
        AuditEventRequestDTO dto = new AuditEventRequestDTO();
        dto.setUserId(1L);
        dto.setEventType(EventType.USER_REGISTERED);
        dto.setDescription("Usuario registrado");
        dto.setResourceId(10L);
        dto.setResourceType("USER");

        AuditEvent event = mapper.toEntity(dto);

        assertThat(event.getId()).isNull();
        assertThat(event.getUserId()).isEqualTo(1L);
        assertThat(event.getEventType()).isEqualTo(EventType.USER_REGISTERED);
        assertThat(event.getDescription()).isEqualTo("Usuario registrado");
        assertThat(event.getResourceId()).isEqualTo(10L);
        assertThat(event.getResourceType()).isEqualTo("USER");
        assertThat(event.getCreatedAt()).isNull();
    }

    @Test
    void toResponseDTO_shouldMapAllFields() {
        AuditEvent event = new AuditEvent();
        event.setId(1L);
        event.setUserId(1L);
        event.setEventType(EventType.USER_REGISTERED);
        event.setDescription("Usuario registrado");
        event.setResourceId(10L);
        event.setResourceType("USER");
        event.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));

        AuditEventResponseDTO dto = mapper.toResponseDTO(event);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getEventType()).isEqualTo(EventType.USER_REGISTERED);
        assertThat(dto.getDescription()).isEqualTo("Usuario registrado");
        assertThat(dto.getResourceId()).isEqualTo(10L);
        assertThat(dto.getResourceType()).isEqualTo("USER");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 30));
    }
}
