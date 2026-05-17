package cl.javiep.auditservice.service;

import cl.javiep.auditservice.dto.AuditEventRequestDTO;
import cl.javiep.auditservice.dto.AuditEventResponseDTO;
import cl.javiep.auditservice.mapper.AuditMapper;
import cl.javiep.auditservice.model.AuditEvent;
import cl.javiep.auditservice.model.EventType;
import cl.javiep.auditservice.repository.AuditEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;
    private final AuditMapper auditMapper;

    public AuditService(AuditEventRepository auditEventRepository, AuditMapper auditMapper) {
        this.auditEventRepository = auditEventRepository;
        this.auditMapper = auditMapper;
    }

    public AuditEventResponseDTO registerEvent(AuditEventRequestDTO dto) {
        AuditEvent event = auditMapper.toEntity(dto);
        AuditEvent savedEvent = auditEventRepository.save(event);
        return auditMapper.toResponseDTO(savedEvent);
    }

    public List<AuditEventResponseDTO> getAllEvents() {
        return auditEventRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(auditMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AuditEventResponseDTO> getEventsByUser(Long userId) {
        return auditEventRepository.findByUserId(userId)
                .stream()
                .map(auditMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AuditEventResponseDTO> getEventsByType(String eventType) {
        EventType type;
        try {
            type = EventType.valueOf(eventType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de evento inválido: " + eventType);
        }
        return auditEventRepository.findByEventType(type)
                .stream()
                .map(auditMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AuditEventResponseDTO> getEventsByResource(Long resourceId, String resourceType) {
        return auditEventRepository.findByResourceIdAndResourceType(resourceId, resourceType)
                .stream()
                .map(auditMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteEvent(Long id) {
        if (!auditEventRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        auditEventRepository.deleteById(id);
    }
}