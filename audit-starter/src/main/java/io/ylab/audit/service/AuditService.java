package io.ylab.audit.service;

import io.ylab.audit.repository.AuditRepository;
import io.ylab.audit.dto.AuditEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service that contains audit business logic.
 */
@RequiredArgsConstructor
public class AuditService {

    /**
     * Injection of repository to persist audit info.
     */
    private final AuditRepository auditRepository;

    public void audit(String info) {
        auditRepository.save(AuditEntity.builder()
                .createdAt(LocalDateTime.now())
                .info(info)
                .build());
    }

    /**
     * Gets list of audits.
     * @return list of audits
     */
    public List<AuditEntity> getAuditInfo() {
        return auditRepository.getAuditItems();
    }
}
