package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AuditEntity;
import io.ylab.wallet.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manipulates audit data via JDBC connection.
 */
public class JdbcAuditRepository {

    public void save(AuditEntity auditItem) {
        String saveAuditSql = """
                INSERT INTO wallet.audit (info, created_at) VALUES (?, ?)
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(saveAuditSql)) {

            preparedStatement.setString(1, auditItem.getInfo());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(auditItem.getCreatedAt()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<AuditEntity> getAuditItems() {
        String getAuditItemsSql = """
                SELECT info, created_at 
                FROM wallet.audit                
                """;
        List<AuditEntity> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(getAuditItemsSql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(AuditEntity.builder()
                        .info(resultSet.getString("info"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return result;
    }
}
