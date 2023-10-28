package io.ylab.wallet.repository;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.entity.AccountEntity;
import io.ylab.wallet.entity.UserEntity;
import io.ylab.wallet.exception.DatabaseException;
import io.ylab.wallet.liquibase.MigrationRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@RequiredArgsConstructor
class JdbcAccountRepositoryTest {

    private static final ConnectionManager connectionManager = new ConnectionManager();
    private static final MigrationRunner migrationRunner = new MigrationRunner(connectionManager);
    private static final JdbcAccountRepository jdbcAccountRepository = new JdbcAccountRepository(connectionManager);

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
    @DisplayName("save new account without user should throw exception")
    void save() {
        AccountEntity accountToSave = AccountEntity.builder()
                .user(UserEntity.builder()
                        .id(4L)
                        .build())
                .balance(BigDecimal.ZERO)
                .build();

        assertThatThrownBy(() -> jdbcAccountRepository.save(accountToSave))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    @DisplayName("update successful")
    void update() {
        AccountEntity account = AccountEntity.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(9999.99))
                .build();

        assertThat(jdbcAccountRepository.getByUserId(1L).get().getBalance())
                .isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
        assertThat(jdbcAccountRepository.updateBalance(account)).isTrue();
        assertThat(jdbcAccountRepository.getByUserId(1L).get().getBalance()).isEqualTo(BigDecimal.valueOf(9999.99));
    }

    @Test
    @DisplayName("update not existing account")
    void updateNotExistingAccount() {
        AccountEntity account = AccountEntity.builder()
                .id(4L)
                .balance(BigDecimal.valueOf(9999.99))
                .build();

        assertThat(jdbcAccountRepository.updateBalance(account)).isFalse();
        assertThat(jdbcAccountRepository.getByUserId(4L)).isEmpty();
    }

    @Test
    @DisplayName("getByUserId return optional of account")
    void getByUserIdSuccess() {
        AccountEntity account = AccountEntity.builder()
                .id(2L)
                .balance(BigDecimal.valueOf(500.00))
                .build();

        Optional<AccountEntity> optionalAccount = jdbcAccountRepository.getByUserId(2L);
        assertThat(optionalAccount).contains(account);
        System.out.println(optionalAccount.get());
    }

    @Test
    @DisplayName("getByUserId returns empty optional")
    void getByUserIdFail() {
        assertThat(jdbcAccountRepository.getByUserId(4L)).isEmpty();
    }
}
