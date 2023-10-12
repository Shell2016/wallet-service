package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.InMemoryAuditStorage;
import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * In-memory implementation of AuditRepository.
 */
@RequiredArgsConstructor
public class InMemoryAuditRepository implements AuditRepository {

    /**
     * Audit data storage.
     */
    private final InMemoryAuditStorage storage;

    /**
     * Saves audit item.
     * @param auditItem
     */
    @Override
    public void addAuditItem(AuditItem auditItem) {
        storage.addAuditItem(auditItem);
    }

    /**
     * Get list of all auditItems.
     * @return list of all audit items
     */
    public List<AuditItem> getAuditItems() {
        return storage.getAuditItems();
    }
}
