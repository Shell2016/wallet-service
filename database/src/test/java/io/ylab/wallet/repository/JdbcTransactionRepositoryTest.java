package io.ylab.wallet.repository;

import io.ylab.wallet.config.ConnectionManager;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.entity.TransactionEntity;
import io.ylab.wallet.exception.DatabaseException;
import io.ylab.wallet.liquibase.MigrationUtils;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class JdbcTransactionRepositoryTest {

    private static final String TRANSACTION_ID_1 = "5400ed4c-c5b7-446a-af44-2940a4c40e90";
    private static final String TRANSACTION_ID_2 = "72d128cc-e317-405f-af55-be22cfd814b1";
    private static final String TRANSACTION_ID_NOT_EXISTS = "5402ed4c-c5b7-446a-af44-2940a4c40e91";
    private static final String TRANSACTION_ID_NEW = "75912588-e2f2-48b8-9ced-6e3d646fd5ae";
    private static final long USER_3_ID = 3L;
    private static final BigDecimal AMOUNT_NEW = BigDecimal.valueOf(333.33);
    private static final BigDecimal AMOUNT_2 = BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_EVEN);
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 10, 10, 10, 10);
    private static final LocalDateTime LOCAL_DATE_TIME_2 =
            Timestamp.valueOf("2023-10-15 15:38:09.207534").toLocalDateTime();
    private static final TransactionEntity TRANSACTION_NEW = TransactionEntity.builder()
            .id(UUID.fromString(TRANSACTION_ID_NEW))
            .userId(USER_3_ID)
            .amount(AMOUNT_NEW)
            .type(TransactionType.WITHDRAW)
            .createdAt(LOCAL_DATE_TIME)
            .build();
    private static final TransactionEntity TRANSACTION_2 = TransactionEntity.builder()
            .id(UUID.fromString(TRANSACTION_ID_2))
            .userId(USER_3_ID)
            .amount(AMOUNT_2)
            .type(TransactionType.DEPOSIT)
            .createdAt(LOCAL_DATE_TIME_2)
            .build();

    private final JdbcTransactionRepository jdbcTransactionRepository = new JdbcTransactionRepository();

    /**
     * New container for each test method to make tests independent.
     */
    @Container
    private final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine");

    /**
     * Configures each container and runs necessary migrations.
     */
    @BeforeEach
    void init() {
        ConnectionManager.setConfig(
                CONTAINER.getJdbcUrl(),
                CONTAINER.getUsername(),
                CONTAINER.getPassword());
        MigrationUtils.setTestChangelog();
        MigrationUtils.setDefaultSchema();
        MigrationUtils.runMigrations();
    }

    @Test
    @DisplayName("check exists() on existing user")
    void existsTrue() {
        assertThat(jdbcTransactionRepository.exists(TRANSACTION_ID_1)).isTrue();
    }

    @Test
    @DisplayName("check exists() on non-existing user")
    void existsFalse() {
        assertThat(jdbcTransactionRepository.exists(TRANSACTION_ID_NOT_EXISTS)).isFalse();
    }

    @Test
    @DisplayName("checks if getAll returns list of right size")
    void getAll() {
        assertThat(jdbcTransactionRepository.getAll()).hasSize(2);
    }

    @Test
    @DisplayName("getAllByUserId() should return list of transactions filtered by user id")
    void getAllByUserId() {
        List<TransactionEntity> transactions = jdbcTransactionRepository.getAllByUserId(USER_3_ID);

        assertThat(transactions).hasSize(1);
        TransactionEntity transaction = transactions.get(0);
        assertThat(transaction).isEqualTo(TRANSACTION_2);
    }

    @Test
    @DisplayName("getAllByUserId() should return empty list")
    void getAllByUserIdEmpty() {
        List<TransactionEntity> transactions = jdbcTransactionRepository.getAllByUserId(5L);

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("save success")
    void save() {
        TransactionEntity transaction = jdbcTransactionRepository.save(TRANSACTION_NEW);

        assertThat(transaction).isNotNull();
        List<TransactionEntity> transactions = jdbcTransactionRepository.getAll();
        assertThat(transactions).hasSize(3);
        TransactionEntity lastTransaction = transactions.get(transactions.size() - 1);
        assertThat(lastTransaction).isEqualTo(TRANSACTION_NEW);
    }

    @Test
    @DisplayName("save with existed id should throw exception")
    void saveWithExistedId() {
        TRANSACTION_NEW.setId(UUID.fromString(TRANSACTION_ID_1));

        assertThatThrownBy(() -> jdbcTransactionRepository.save(TRANSACTION_NEW))
                .isInstanceOf(DatabaseException.class);
    }
}
