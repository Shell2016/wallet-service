package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.entity.*;
import io.ylab.wallet.domain.exception.TransactionException;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private static final String UUID_TRANSACTION1 = "adde1e02-1784-4973-956c-80d064309d55";
    private static final String UUID_TRANSACTION2 = "adde1e02-1784-4973-956c-80d064309d56";
    private static final String UUID_TRANSACTION3 = "adde1e02-1784-4973-956c-80d064309d57";
    private static final String UUID_USER_STRING1 = "cc6227ae-6a83-4888-9538-df7062c572fe";
    private static final String UUID_USER_STRING2 = "cc6227ae-6a83-4888-9537-df7062c572fe";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(10000);
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(
            2023, 10, 9, 12, 0);
    private static final Transaction TRANSACTION1_USER1 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION1))
            .userId(UUID.fromString(UUID_USER_STRING1))
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final Transaction TRANSACTION2_USER1 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION2))
            .userId(UUID.fromString(UUID_USER_STRING1))
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final Transaction TRANSACTION1_USER2 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION3))
            .userId(UUID.fromString(UUID_USER_STRING2))
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final String TRANSACTION_EXISTS_ERROR_MESSAGE =
            "Транзакция с id=" + UUID_TRANSACTION1 + " уже зарегистрирована в системе!\n" +
                    "Операция отклонена!";
    private static final User USER = new User(UUID.fromString(UUID_USER_STRING1), "testname", "123456");

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final TransactionService transactionService = new TransactionService(transactionRepository, userService);

    @Test
    void transactionExists() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(true);

        assertThat(transactionService.transactionExists(UUID_TRANSACTION1)).isTrue();
    }

    @Test
    void getUserTransactions() {
        when(transactionRepository.getAll())
                .thenReturn(new ArrayList<>(List.of(TRANSACTION1_USER1, TRANSACTION2_USER1, TRANSACTION1_USER2)));

        List<Transaction> transactions = transactionService.getUserTransactions(UUID_USER_STRING1);
        assertThat(transactions)
                .hasSize(2)
                .containsExactly(TRANSACTION1_USER1, TRANSACTION2_USER1);
    }

    @Test
    void processTransactionIfAlreadyExistsThrowsException() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(true);

        assertThatThrownBy(() -> transactionService
                .processTransaction(UUID_TRANSACTION1, UUID_USER_STRING1, TransactionType.DEPOSIT, AMOUNT.toString()))
                .isInstanceOf(TransactionException.class)
                .hasMessage(TRANSACTION_EXISTS_ERROR_MESSAGE)
                .hasNoCause();
    }

    @Test
    void processTransactionNotThrowsException() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(false);
        when(userService.getUserById(any())).thenReturn(Optional.of(USER));

        assertThatNoException().isThrownBy(() -> transactionService
                .processTransaction(UUID_TRANSACTION1, UUID_USER_STRING1, TransactionType.DEPOSIT, AMOUNT.toString()));
    }

    @Test
    void processTransactionSuccess() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(false);
        when(userService.getUserById(any())).thenReturn(Optional.of(USER));
        when(transactionRepository.save(TRANSACTION1_USER1)).thenReturn(TRANSACTION1_USER1);

        Transaction transaction = transactionService
                .processTransaction(UUID_TRANSACTION1, UUID_USER_STRING1, TransactionType.DEPOSIT, AMOUNT.toString());

        assertThat(transaction).isEqualTo(TRANSACTION1_USER1);
    }
}
