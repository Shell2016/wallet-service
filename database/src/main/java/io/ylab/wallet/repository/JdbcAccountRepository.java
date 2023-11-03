package io.ylab.wallet.repository;

import io.ylab.wallet.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Manipulates account data via JDBC connection.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository {

    /**
     * JdbcTemplate for sql querying.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Persists given account.
     * @return AccountEntity with generated id
     */
    public AccountEntity save(AccountEntity account) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("wallet")
                .withTableName("account")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", account.getUser().getId());
        params.put("balance", BigDecimal.ZERO);
        long id = insert.executeAndReturnKey(params).longValue();
        account.setId(id);
        return account;
    }

    /**
     * Updates balance of given account.
     * @return true if successful
     */
    public boolean updateBalance(AccountEntity account) {
        String sql = """
                UPDATE wallet.account\s
                SET balance = ?\s
                WHERE id = ?
                """;
        int updatedRows = jdbcTemplate.update(sql, account.getBalance(), account.getId());
        return updatedRows == 1;
    }

    /**
     * Gets AccountEntity by userId.
     */
    public Optional<AccountEntity> getByUserId(long userId) {
        String sql = """
                SELECT id, balance FROM wallet.account where user_id = ?
                """;
        AccountEntity account = null;
        try {
            account = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    AccountEntity.builder()
                            .id(rs.getLong("id"))
                            .balance(rs.getBigDecimal("balance"))
                            .build(),
                    userId);
        } catch (DataAccessException e) {
            log.debug("Account with user id " + userId + " was not found!");
        }
        return Optional.ofNullable(account);
    }
}
