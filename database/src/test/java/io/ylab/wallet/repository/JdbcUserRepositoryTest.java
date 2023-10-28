package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AccountEntity;
import io.ylab.wallet.entity.UserEntity;
import io.ylab.wallet.liquibase.MigrationRunner;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class JdbcUserRepositoryTest {

    private static final String USERNAME_1 = "user1";
    private static final String PASSWORD = "123456";
    private static final long USER_1_ID = 1L;
    private static final UserEntity USER_1 = UserEntity.builder()
            .id(USER_1_ID)
            .username(USERNAME_1)
            .password(PASSWORD)
            .build();
    private static final String USERNAME_NOT_EXIST = "user_not_exists";
    private static final long USER_ID_NOT_EXISTS = 100L;
    private static final long ID_NEW = 4L;
    private static final String USERNAME_NEW = "new_user";

    private static final ConnectionManager connectionManager = new ConnectionManager();
    private static final MigrationRunner migrationRunner = new MigrationRunner(connectionManager);
    private final JdbcAccountRepository jdbcAccountRepository = new JdbcAccountRepository(connectionManager);
    private final JdbcUserRepository jdbcUserRepository =
            new JdbcUserRepository(connectionManager, jdbcAccountRepository);

    /**
     * One container per class because in this class - method test logic is independent.
     */
    @Container
    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine");

    /**
     * Configures each container and runs necessary migrations.
     */
    @BeforeAll
    static void init() {
        connectionManager.setConfig(
                CONTAINER.getJdbcUrl(),
                CONTAINER.getUsername(),
                CONTAINER.getPassword());
        migrationRunner.runTestMigrations();
    }

    @Test
    @DisplayName("Check if user exists should return true")
    void existByUsername() {
        boolean exists = jdbcUserRepository.existsByUsername(USERNAME_1);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Check if user exists should return false")
    void existByUsernameFalse() {
        boolean exists = jdbcUserRepository.existsByUsername(USERNAME_NOT_EXIST);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("getByUsername should return optional of user")
    void getByUserName() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getByUsername(USERNAME_1);

        assertThat(optionalUser).contains(USER_1);
    }

    @Test
    @DisplayName("getByUsername should return empty optional")
    void getByUserNameIsEmpty() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getByUsername(USERNAME_NOT_EXIST);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("getById should return optional of user")
    void getById() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getById(USER_1_ID);

        assertThat(optionalUser).contains(USER_1);
    }

    @Test
    @DisplayName("getById should return empty optional")
    void getByIdIsEmpty() {
        Optional<UserEntity> optionalUser = jdbcUserRepository.getById(USER_ID_NOT_EXISTS);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("save user with account")
    void save() {
        UserEntity expectedUser = UserEntity.builder()
                .id(ID_NEW)
                .username(USERNAME_NEW)
                .password("111111")
                .build();
        AccountEntity account = AccountEntity.builder()
                .id(ID_NEW)
                .user(expectedUser)
                .balance(BigDecimal.ZERO)
                .build();
        expectedUser.setAccount(account);

        assertThat(jdbcUserRepository.existsByUsername(USERNAME_NEW)).isFalse();
        assertThat(jdbcAccountRepository.getByUserId(ID_NEW)).isEmpty();
        UserEntity newUser = jdbcUserRepository.save(UserEntity.builder()
                .username(USERNAME_NEW)
                .password("111111")
                .build());
        assertThat(newUser).isEqualTo(expectedUser);
        assertThat(jdbcUserRepository.existsByUsername(USERNAME_NEW)).isTrue();
        assertThat(jdbcAccountRepository.getByUserId(ID_NEW)).isNotEmpty();
    }
}
