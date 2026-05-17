package cl.javiep.auditservice.mapper;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.model.AuditEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditEvent toEntity(AuditEventRequestDTO dto) {
        AuditEvent event = new AuditEvent();
        event.setUserId(dto.getUserId());
        event.setEventType(dto.getEventType());
        event.setDescription(dto.getDescription());
        event.setResourceId(dto.getResourceId());
        event.setResourceType(dto.getResourceType());
        return event;
    }

    public AuditEventResponseDTO toResponseDTO(AuditEvent event) {
        AuditEventResponseDTO dto = new AuditEventResponseDTO();
        dto.setId(event.getId());
        dto.setUserId(event.getUserId());
        dto.setEventType(event.getEventType());
        dto.setDescription(event.getDescription());
        dto.setResourceId(event.getResourceId());
        dto.setResourceType(event.getResourceType());
        dto.setCreatedAt(event.getCreatedAt());
        return dto;
    }
}