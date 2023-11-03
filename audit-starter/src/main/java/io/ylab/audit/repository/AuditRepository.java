package io.ylab.audit.repository;

import io.ylab.audit.dto.AuditEntity;

import java.util.List;

/**
 * Interface for interacting with audit data storage.
 */
public interface AuditRepository {
    void save(AuditEntity auditItem);
    List<AuditEntity> getAuditItems();
}
