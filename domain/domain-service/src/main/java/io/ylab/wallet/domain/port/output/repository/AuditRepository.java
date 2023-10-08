package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.dto.AuditItem;

import java.util.List;

public interface AuditRepository {
    void addAuditItem(AuditItem auditItem);
    List<AuditItem> getAuditInfo();
}
