package io.ylab.wallet.repository;

import io.ylab.wallet.entity.AuditEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manipulates audit data via JDBC connection.
 */
@RequiredArgsConstructor
@Component
public class JdbcAuditRepository {

    /**
     * JdbcTemplate for sql querying.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Persists audit info into database.
     * @param auditItem with audit info
     */
    public void save(AuditEntity auditItem) {
        String sql = """
                INSERT INTO wallet.audit (info, created_at) VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, auditItem.getInfo(), auditItem.getCreatedAt());
    }

    /**
     * Gets list of all audit items.
     */
    public List<AuditEntity> getAuditItems() {
        String sql = """
                SELECT info, created_at\s
                FROM wallet.audit
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> AuditEntity.builder()
                .info(rs.getString("info"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build());
    }
}
