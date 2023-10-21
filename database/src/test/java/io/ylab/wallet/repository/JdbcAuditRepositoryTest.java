package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AuditEntity;
import io.ylab.wallet.liquibase.MigrationUtils;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class JdbcAuditRepositoryTest {

    private final JdbcAuditRepository jdbcAuditRepository = new JdbcAuditRepository();

    /**
     * New container for each test method to make tests independent.
     */
    @Container
    private final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine");

    /**
     * Configures each container and runs necessary migrations.
     */
    @BeforeEach
    void init() {
        ConnectionManager.setConfig(
                CONTAINER.getJdbcUrl(),
                CONTAINER.getUsername(),
                CONTAINER.getPassword());
        MigrationUtils.setTestChangelog();
        MigrationUtils.setDefaultSchema();
        MigrationUtils.runMigrations();
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
