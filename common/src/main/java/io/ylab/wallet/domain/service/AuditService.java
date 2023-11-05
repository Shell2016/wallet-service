package io.ylab.wallet.domain.service;

/**
 * AuditService Interface that implemented in domain-service and used by audit-starter.
 */
public interface AuditService {
    void audit(String info);
}
