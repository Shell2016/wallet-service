package io.ylab.wallet.repository;

import io.ylab.wallet.config.ConnectionManager;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.entity.TransactionEntity;
import io.ylab.wallet.exception.DatabaseException;

import java.sql.*;
import java.util.*;

/**
 * Manipulates transaction data via JDBC connection.
 */
public class JdbcTransactionRepository {

    public boolean exists(String id) {
        String sql = """
                SELECT 1 FROM wallet.transaction where id = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, UUID.fromString(id), Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return false;
    }

    public TransactionEntity save(TransactionEntity transaction) {
        String sql = """
                INSERT INTO wallet.transaction (id, user_id, amount, type, created_at)\s
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, transaction.getId(), Types.OTHER);
            preparedStatement.setLong(2, transaction.getUserId());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getType().name());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getCreatedAt()));
            if (preparedStatement.executeUpdate() == 1) {
                return transaction;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }

    public List<TransactionEntity> getAll() {
        String sql = """
                SELECT id, user_id, amount, type, created_at\s
                FROM wallet.transaction
                """;
        List<TransactionEntity> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            populateResultList(result, preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return result;
    }

    public List<TransactionEntity> getAllByUserId(long userId) {
        String sql = """
                SELECT id, user_id, amount, type, created_at\s
                FROM wallet.transaction\s
                WHERE user_id = ?
                """;
        List<TransactionEntity> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);
            populateResultList(result, preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return result;
    }

    private void populateResultList(List<TransactionEntity> result, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result.add(TransactionEntity.builder()
                    .id(resultSet.getObject("id", UUID.class))
                    .userId(resultSet.getLong("user_id"))
                    .amount(resultSet.getBigDecimal("amount"))
                    .type(TransactionType.valueOf(resultSet.getString("type")))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build());
        }
    }
}
