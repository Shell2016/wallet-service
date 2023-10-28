package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AccountEntity;
import io.ylab.wallet.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Manipulates account data via JDBC connection.
 */
@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository {

    private final ConnectionManager connectionManager;

    public AccountEntity save(AccountEntity account) {
        try (Connection connection = connectionManager.open()) {
            account =  save(account, connection);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return account;
    }

    public AccountEntity save(AccountEntity account, Connection connection) throws SQLException {
        String createAccountSql = """
                INSERT INTO wallet.account (user_id, balance) VALUES (?, ?)
                """;
        long accountId;
        try (PreparedStatement createAccountStatement =
                     connection.prepareStatement(createAccountSql, Statement.RETURN_GENERATED_KEYS)) {
            createAccountStatement.setLong(1, account.getUser().getId());
            createAccountStatement.setBigDecimal(2, account.getBalance());
            createAccountStatement.executeUpdate();
            ResultSet generatedKeys = createAccountStatement.getGeneratedKeys();
            generatedKeys.next();
            accountId = generatedKeys.getLong("id");
        }
        account.setId(accountId);
        return account;
    }

    public boolean updateBalance(AccountEntity account) {
        String sql = """
                UPDATE wallet.account 
                SET balance = ? 
                WHERE id = ?
                """;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBigDecimal(1, account.getBalance());
            preparedStatement.setLong(2, account.getId());
            if (preparedStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return false;
    }

    public Optional<AccountEntity> getByUserId(long userId) {
        String sql = """
                SELECT id, balance FROM wallet.account where user_id = ?
                """;
        AccountEntity account = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = AccountEntity.builder()
                        .id(resultSet.getLong("id"))
                        .balance(resultSet.getBigDecimal("balance"))
                        .build();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return Optional.ofNullable(account);
    }
}
