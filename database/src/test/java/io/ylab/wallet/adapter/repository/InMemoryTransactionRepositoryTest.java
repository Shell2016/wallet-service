package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.InMemoryTransactionStorage;
import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTransactionRepositoryTest {

    private static final String UUID_TRANSACTION_STRING = "8eeb3622-5c1b-4067-9f94-1dc22fac11b9";
    private static final String UUID_TRANSACTION_STRING2 = "8eeb3622-5c1b-4067-9f94-1dc22fac11b8";
    private static final String UUID_USER_STRING = "cc6227ae-6a83-4888-9538-df7062c572fe";
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 10, 9, 12, 0);
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(10000);
    private static final Transaction TRANSACTION = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION_STRING))
            .userId(UUID.fromString(UUID_USER_STRING))
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final Transaction TRANSACTION2 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION_STRING2))
            .userId(UUID.fromString(UUID_USER_STRING))
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private TransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        transactionRepository = new InMemoryTransactionRepository(new InMemoryTransactionStorage());
    }

    @Test
    void checkExistsIfDatabaseIsEmpty() {
        assertThat(transactionRepository.exists(UUID_TRANSACTION_STRING)).isFalse();
    }

    @Test
    void saveAndCheckExists() {
        transactionRepository.save(TRANSACTION);

        assertThat(transactionRepository.exists(UUID_TRANSACTION_STRING)).isTrue();
    }

    @Test
    void getAllCheck() {
        transactionRepository.save(TRANSACTION);
        transactionRepository.save(TRANSACTION2);

        assertThat(transactionRepository.getAll()).hasSize(2);
    }
}
