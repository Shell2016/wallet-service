package io.ylab.wallet.repository;

import io.ylab.wallet.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Manipulates user data via JDBC connection.
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcUserRepository {

    /**
     * JdbcTemplate for sql querying.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Persists user into database.
     *
     * @param user to save
     * @return UserEntity with generated id set
     */
    public UserEntity save(UserEntity user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("wallet")
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        long id = insert.executeAndReturnKey(params).longValue();
        user.setId(id);
        return user;
    }

    /**
     * Checks if user exists.
     *
     * @param username of the user to find
     * @return true if user with given username exists
     */
    public boolean existsByUsername(String username) {
        String sql = """
                SELECT COUNT(*) FROM wallet.users where username = ? LIMIT 1
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count == 1;
    }

    /**
     * Gets user by username (with password)
     *
     * @param username of user to find
     * @return user with password
     */
    public Optional<UserEntity> getByUsername(String username) {
        String sql = """
                SELECT id, password FROM wallet.users where username = ?
                """;
        UserEntity user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> UserEntity.builder()
                            .id(rs.getLong("id"))
                            .username(username)
                            .password(rs.getString("password"))
                            .build(),
                    username
            );
        } catch (DataAccessException e) {
            log.debug("Cannot find user with username: " + username);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Gets user without password
     *
     * @param id of user to find
     * @return user without password
     */
    public Optional<UserEntity> getById(long id) {
        String sql = """
                SELECT username FROM wallet.users where id = ?
                """;
        UserEntity user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    UserEntity.builder()
                            .id(id)
                            .username(rs.getString("username"))
                            .build(), id);
        } catch (DataAccessException e) {
            log.debug("Cannot find user with id: " + id);
        }
        return Optional.ofNullable(user);
    }
}
