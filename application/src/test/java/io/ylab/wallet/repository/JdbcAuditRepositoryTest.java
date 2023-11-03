package io.ylab.wallet.repository;

import io.ylab.audit.dto.AuditEntity;
import io.ylab.audit.repository.JdbcAuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcAuditRepositoryTest extends JdbcIntegrationTestBase {

    private final JdbcAuditRepository jdbcAuditRepository;

    @Autowired
    public JdbcAuditRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcAuditRepository = new JdbcAuditRepository(jdbcTemplate);
    }

    @Test
    void getAuditItems() {
        assertThat(jdbcAuditRepository.getAuditItems()).hasSize(2);
    }

    @Test
    @DisplayName("save new audit item")
    void save() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 10, 10, 0);
        AuditEntity newAudit = AuditEntity.builder()
                .info("new_audit")
                .createdAt(createdAt)
                .build();

        assertThat(jdbcAuditRepository.getAuditItems()).hasSize(2);
        jdbcAuditRepository.save(newAudit);
        List<AuditEntity> auditItems = jdbcAuditRepository.getAuditItems();
        assertThat(auditItems).hasSize(3);
        AuditEntity savedAudit = auditItems.get(2);
        assertThat(savedAudit.getInfo()).isEqualTo("new_audit");
        assertThat(savedAudit.getCreatedAt()).isEqualTo(createdAt);
    }
}
