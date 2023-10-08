package io.ylab.wallet.database.storage.audit;

import java.util.*;

public class InMemoryAuditStorage {
    private static final List<AuditItem> auditStorage = new ArrayList<>();

    public List<AuditItem> getAuditInfo() {
        return auditStorage;
    }

    public void addAudit(AuditItem audit) {
        auditStorage.add(audit);
    }

}
