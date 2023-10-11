package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WithdrawalStateTest {

    public static final String USER_ID = "0ff72a9a-a7a3-4c17-a25e-b5ef5215d528";
    public static final String ESC = "esc";
    public static final String TRANSACTION_INPUT = "7272c49b-3736-4819-9159-222de2021ed3 1000";
    public static final String AMOUNT = "1000";
    public static final String TRANSACTION_ID = "7272c49b-3736-4819-9159-222de2021ed3";
    public static final Transaction TRANSACTION = Transaction.builder()
            .id(UUID.fromString(TRANSACTION_ID))
            .userId(UUID.fromString(USER_ID))
            .amount(new BigDecimal(AMOUNT))
            .type(TransactionType.WITHDRAW)
            .createdAt(LocalDateTime.of(2023, 10, 11 , 13, 0))
            .build();
    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new WithdrawalState(applicationService);

    @BeforeEach
    void init() {
        State.setContext(USER_ID);
    }

    @Test
    void processInputEsc() {
        state.processInput(ESC);

        verify(applicationService).setState(AuthorizedState.class);
        verify(applicationService).audit("Transaction cancelled by user " + USER_ID);
    }

    @Test
    void processInputTransactionSuccess() {
        when(applicationService.processTransaction(TRANSACTION_ID, TransactionType.WITHDRAW, AMOUNT))
                .thenReturn(TRANSACTION);

        state.processInput(TRANSACTION_INPUT);

        verify(applicationService).setState(AuthorizedState.class);
        verify(applicationService).audit("Transaction success: " + TRANSACTION);
    }
}