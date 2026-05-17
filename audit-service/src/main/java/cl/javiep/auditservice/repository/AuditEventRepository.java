package cl.javiep.auditservice.repository;

import cl.javiep.auditservice.model.AuditEvent;
import cl.javiep.auditservice.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {

    List<AuditEvent> findByUserId(Long userId);

    List<AuditEvent> findByEventType(EventType eventType);

    List<AuditEvent> findByResourceIdAndResourceType(Long resourceId, String resourceType);

    List<AuditEvent> findByUserIdAndEventType(Long userId, EventType eventType);

    List<AuditEvent> findAllByOrderByCreatedAtDesc();
}