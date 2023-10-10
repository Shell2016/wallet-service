package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.InMemoryAuditStorage;
import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryAuditRepositoryTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 10, 9, 12, 0);
    private static final String SOME_AUDIT_INFO = "Some audit info";
    private static final AuditItem AUDIT_ITEM = AuditItem.builder()
            .time(LOCAL_DATE_TIME)
            .info(SOME_AUDIT_INFO)
            .build();

    private AuditRepository repository;

    @BeforeEach
    void init() {
        repository = new InMemoryAuditRepository(new InMemoryAuditStorage());
    }

    @Test
    void addAuditItem() {
        repository.addAuditItem(AUDIT_ITEM);
        repository.addAuditItem(AUDIT_ITEM);

        List<AuditItem> auditItems = repository.getAuditInfo();
        assertThat(auditItems).hasSize(2);
        auditItems.forEach(item -> assertThat(item).isEqualTo(AUDIT_ITEM));
    }
}