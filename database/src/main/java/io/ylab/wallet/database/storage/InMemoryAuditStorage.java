package io.ylab.wallet.database.storage;

import io.ylab.wallet.domain.dto.AuditItem;

import java.util.*;

/**
 * In-memory storage for audit data.
 */
public class InMemoryAuditStorage {
    private static final List<AuditItem> auditStorage = new ArrayList<>();

    public List<AuditItem> getAuditInfo() {
        return auditStorage;
    }

    public void addAuditItem(AuditItem audit) {
        auditStorage.add(audit);
    }

}
