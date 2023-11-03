package io.ylab.wallet.repository;

import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.entity.TransactionEntity;
import io.ylab.wallet.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Manipulates transaction data via JDBC connection.
 */
@Repository
@RequiredArgsConstructor
public class JdbcTransactionRepository {

    /**
     * JdbcTemplate for sql querying.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * TransactionEntity row mapper.
     */
    private final RowMapper<TransactionEntity> transactionEntityRowMapper = (rs, rowNum) ->
            TransactionEntity.builder()
                    .id(rs.getObject("id", UUID.class))
                    .userId(rs.getLong("user_id"))
                    .amount(rs.getBigDecimal("amount"))
                    .type(TransactionType.valueOf(rs.getString("type")))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();

    /**
     * Checks if transaction with given id exists.
     *
     * @param id uuid of transaction
     * @return true if exists
     */
    public boolean exists(String id) {
        String sql = """
                SELECT COUNT(*) FROM wallet.transaction where id = ? LIMIT 1
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, UUID.fromString(id));
        return count != null && count == 1;
    }

    /**
     * Persists given transaction in database.
     */
    public TransactionEntity save(TransactionEntity transaction) {
        String sql = """
                INSERT INTO wallet.transaction (id, user_id, amount, type, created_at)
                 VALUES (?, ?, ?, ?, ?)
                """;
        int updated = jdbcTemplate.update(sql,
                transaction.getId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getType().name(),
                transaction.getCreatedAt());
        if (updated != 1) {
            throw new DatabaseException("Database error while saving transaction: " + transaction);
        }
        return transaction;
    }

    /**
     * Gets list of all transactions.
     */
    public List<TransactionEntity> getAll() {
        String sql = """
                SELECT id, user_id, amount, type, created_at
                 FROM wallet.transaction
                """;
        return jdbcTemplate.query(sql, transactionEntityRowMapper);
    }

    /**
     * Gets list of all user's transactions.
     *
     * @param userId id of user
     */
    public List<TransactionEntity> getAllByUserId(long userId) {
        String sql = """
                SELECT id, user_id, amount, type, created_at\s
                FROM wallet.transaction\s
                WHERE user_id = ?
                """;
        return jdbcTemplate.query(sql, transactionEntityRowMapper, userId);
    }
}
