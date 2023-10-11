package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class AuthorizedStateTest {

    public static final String USER_ID = "0ff72a9a-a7a3-4c17-a25e-b5ef5215d528";
    public static final String VIEW_BALANCE_REQUEST = "1";
    public static final String DEPOSIT_REQUEST = "2";
    public static final String WITHDRAWAL_REQUEST = "3";
    public static final String TRANSACTION_HISTORY_REQUEST = "4";
    public static final String LOGOUT_REQUEST = "5";
    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new AuthorizedState(applicationService);

    @BeforeEach
    void init() {
        State.setContext(USER_ID);
    }

    @Test
    void processInputBalance() {
        state.processInput(VIEW_BALANCE_REQUEST);

        verify(applicationService).setState(ViewBalanceState.class);
        verify(applicationService).audit("Balance request from user with id: " + USER_ID);
    }

    @Test
    void processInputDeposit() {
        state.processInput(DEPOSIT_REQUEST);

        verify(applicationService).setState(DepositState.class);
        verify(applicationService).audit("Initiated deposit transaction by user with id " + USER_ID);
    }

    @Test
    void processInputWithdrawal() {
        state.processInput(WITHDRAWAL_REQUEST);

        verify(applicationService).setState(WithdrawalState.class);
        verify(applicationService).audit("Initiated withdrawal transaction by user with id " + USER_ID);
    }

    @Test
    void processInputTransactionHistory() {
        state.processInput(TRANSACTION_HISTORY_REQUEST);

        verify(applicationService).setState(TransactionHistoryState.class);
        verify(applicationService).audit("Requested transaction history by user with id " + USER_ID);
    }

    @Test
    void processInputLogout() {
        state.processInput(LOGOUT_REQUEST);

        assertThat(State.getContext()).isNull();
        verify(applicationService).setState(StartState.class);
        verify(applicationService).audit("Successful logout: userId=" +  USER_ID);
    }

}