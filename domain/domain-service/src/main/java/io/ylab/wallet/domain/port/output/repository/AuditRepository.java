package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.dto.AuditItem;

import java.util.List;

/**
 * Interface for interacting with audit data storage.
 * Acts as output port in onion architecture.
 */
public interface AuditRepository {
    void addAuditItem(AuditItem auditItem);
    List<AuditItem> getAuditInfo();
}
