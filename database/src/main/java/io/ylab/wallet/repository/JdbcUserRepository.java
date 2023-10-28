package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AccountEntity;
import io.ylab.wallet.entity.UserEntity;
import io.ylab.wallet.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

/**
 * Manipulates user data via JDBC connection.
 */
@RequiredArgsConstructor
@Repository
public class JdbcUserRepository {

    private final ConnectionManager connectionManager;

    /**
     * JDBC implementation of account repository.
     */
    private final JdbcAccountRepository accountRepository;

    /**
     * Creates user with account in one transaction.
     * @param user to save
     * @return UserEntity with account set
     */
    public UserEntity save(UserEntity user) {
        String createUserSql = """
                INSERT INTO wallet.users (username, password) VALUES (?, ?)
                """;
        Connection connection = null;
        PreparedStatement createUserStatement = null;
        try {
            connection = connectionManager.open();
            connection.setAutoCommit(false);
            createUserStatement = connection.prepareStatement(createUserSql, Statement.RETURN_GENERATED_KEYS);
            createUserStatement.setString(1, user.getUsername());
            createUserStatement.setString(2, user.getPassword());
            createUserStatement.executeUpdate();
            ResultSet generatedKeys = createUserStatement.getGeneratedKeys();
            generatedKeys.next();
            long userId = generatedKeys.getLong("id");
            user.setId(userId);
            AccountEntity account = AccountEntity.builder()
                    .user(user)
                    .balance(BigDecimal.ZERO)
                    .build();
            AccountEntity savedAccount = accountRepository.save(account, connection);
            user.setAccount(savedAccount);
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена: " + e.getMessage());
                } catch (SQLException rollbackException) {
                    System.err.println("Ошибка при откате транзакции: " + rollbackException.getMessage());
                    throw new DatabaseException(rollbackException.getMessage());
                }
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (createUserStatement != null) {
                    createUserStatement.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
        return user;
    }

    public boolean existsByUsername(String username) {
        String sql = """
                SELECT 1 FROM wallet.users where username = ?
                """;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return false;
    }

    /**
     * Gets user by username (with password)
     * @param username of user to find
     * @return user with password
     */
    public Optional<UserEntity> getByUsername(String username) {
        String sql = """
                SELECT id, password FROM wallet.users where username = ?
                """;
        UserEntity user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = UserEntity.builder()
                        .id(resultSet.getLong("id"))
                        .username(username)
                        .password(resultSet.getString("password"))
                        .build();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    /**
     * Gets user without password
     * @param id of user to find
     * @return user without password
     */
    public Optional<UserEntity> getById(long id) {
        String sql = """
                SELECT username FROM wallet.users where id = ?
                """;
        UserEntity user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = UserEntity.builder()
                        .id(id)
                        .username(resultSet.getString("username"))
                        .build();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return Optional.ofNullable(user);
    }
}
