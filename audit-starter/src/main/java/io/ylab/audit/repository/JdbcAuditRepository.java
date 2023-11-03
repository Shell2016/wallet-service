package io.ylab.audit.repository;

import io.ylab.audit.dto.AuditEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Manipulates audit data via JDBC connection.
 */
@RequiredArgsConstructor
public class JdbcAuditRepository implements AuditRepository {

    /**
     * JdbcTemplate for sql querying.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Persists audit info into database.
     * @param auditItem with audit info
     */
    @Override
    public void save(AuditEntity auditItem) {
        String sql = """
                INSERT INTO wallet.audit (info, created_at) VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, auditItem.getInfo(), auditItem.getCreatedAt());
    }

    /**
     * Gets list of all audit items.
     */
    @Override
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
