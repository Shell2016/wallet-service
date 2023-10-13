package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

class ViewBalanceStateTest {

    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new ViewBalanceState(applicationService);

    @Test
    void processInput() {
        state.processInput(anyString());

        verify(applicationService).setState(AuthorizedState.class);
    }
}