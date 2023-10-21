package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.TransactionDto;
import io.ylab.wallet.domain.dto.TransactionRequest;
import io.ylab.wallet.domain.entity.*;
import io.ylab.wallet.domain.exception.TransactionException;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("Transaction Service Test")
class TransactionServiceTest {

    private static final String UUID_TRANSACTION1 = "adde1e02-1784-4973-956c-80d064309d55";
    private static final String UUID_TRANSACTION2 = "adde1e02-1784-4973-956c-80d064309d56";
    private static final long USER_1_ID = 1L;
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(10000);
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(
            2023, 10, 9, 12, 0);
    private static final TransactionRequest TRANSACTION_REQUEST =
            new TransactionRequest(UUID_TRANSACTION1, "deposit", AMOUNT.toString());
    private static final Transaction TRANSACTION1_USER1 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION1))
            .userId(USER_1_ID)
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final Transaction TRANSACTION2_USER1 = Transaction.builder()
            .id(UUID.fromString(UUID_TRANSACTION2))
            .userId(USER_1_ID)
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final TransactionDto TRANSACTION1_USER1_DTO = TransactionDto.builder()
            .id(UUID.fromString(UUID_TRANSACTION1))
            .userId(USER_1_ID)
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final TransactionDto TRANSACTION2_USER1_DTO = TransactionDto.builder()
            .id(UUID.fromString(UUID_TRANSACTION2))
            .userId(USER_1_ID)
            .amount(AMOUNT)
            .createdAt(LOCAL_DATE_TIME)
            .type(TransactionType.DEPOSIT)
            .build();
    private static final String TRANSACTION_EXISTS_ERROR_MESSAGE =
            "Транзакция с id=" + UUID_TRANSACTION1 + " уже зарегистрирована в системе!\n" +
                    "Операция отклонена!";
    private static final User USER = User.builder()
            .id(USER_1_ID)
            .username("testname")
            .password("123456")
            .account(Account.builder()
                    .balance(BigDecimal.ZERO)
                    .build())
            .build();

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final AccountService accountService = Mockito.mock(AccountService.class);
    private final TransactionService transactionService =
            new TransactionService(transactionRepository, userService, accountService);

    @Test
    @DisplayName("transactionExists")
    void transactionExists() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(true);

        assertThat(transactionService.transactionExists(UUID_TRANSACTION1)).isTrue();
    }

    @Test
    @DisplayName("getUserTransactions")
    void getUserTransactions() {
        when(transactionRepository.getAllByUserId(USER_1_ID))
                .thenReturn(new ArrayList<>(List.of(TRANSACTION1_USER1, TRANSACTION2_USER1)));

        List<TransactionDto> transactions = transactionService.getUserTransactions(USER_1_ID);
        assertThat(transactions)
                .hasSize(2)
                .containsExactly(TRANSACTION1_USER1_DTO, TRANSACTION2_USER1_DTO);
    }

    @Test
    @DisplayName("Processing existed transaction should throw exception")
    void processTransactionIfAlreadyExistsThrowsException() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(true);

        assertThatThrownBy(() -> transactionService
                .processTransaction(TRANSACTION_REQUEST, USER_1_ID))
                .isInstanceOf(TransactionException.class)
                .hasMessage(TRANSACTION_EXISTS_ERROR_MESSAGE)
                .hasNoCause();
    }

    @Test
    @DisplayName("Processing not existed transaction should not throw exception")
    void processTransactionNotThrowsException() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(false);
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(USER));

        Assertions.assertThatNoException().isThrownBy(() -> transactionService
                .processTransaction(TRANSACTION_REQUEST, USER_1_ID));
    }

    @Test
    @DisplayName("processing successful transaction")
    void processTransactionSuccess() {
        when(transactionRepository.exists(UUID_TRANSACTION1)).thenReturn(false);
        when(userService.getUserById(USER_1_ID)).thenReturn(Optional.of(USER));
        when(accountService.updateAccountBalance(USER.getAccount())).thenReturn(true);
        when(transactionRepository.save(TRANSACTION1_USER1)).thenReturn(TRANSACTION1_USER1);

        TransactionDto transaction = transactionService
                .processTransaction(TRANSACTION_REQUEST, USER_1_ID);

        assertThat(transaction).isEqualTo(TRANSACTION1_USER1_DTO);
    }
}
