package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.audit.InMemoryAuditStorage;
import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InMemoryAuditRepository implements AuditRepository {

    private final InMemoryAuditStorage storage;

    @Override
    public void addAuditItem(AuditItem auditItem) {
        storage.addAuditItem(auditItem);
    }

    public List<AuditItem> getAuditInfo() {
        return storage.getAuditInfo();
    }
}
