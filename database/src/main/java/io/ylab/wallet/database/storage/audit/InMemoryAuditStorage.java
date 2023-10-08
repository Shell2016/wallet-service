package io.ylab.wallet.database.storage.audit;

import io.ylab.wallet.domain.dto.AuditItem;

import java.util.*;

public class InMemoryAuditStorage {
    private static final List<AuditItem> auditStorage = new ArrayList<>();

    public List<AuditItem> getAuditInfo() {
        return auditStorage;
    }

    public void addAuditItem(AuditItem audit) {
        auditStorage.add(audit);
    }

}
