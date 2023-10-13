package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class StartStateTest {

    private static final String REGISTRATION_REQUEST = "2";
    private static final String LOGIN_REQUEST = "1";
    private static final String AUDIT_REQUEST = "getAudit";
    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new StartState(applicationService);

    @Test
    void processInputRegistration() {
        state.processInput(REGISTRATION_REQUEST);

        verify(applicationService).setState(RegistrationGetNameState.class);
    }

    @Test
    void processInputLogin() {
        state.processInput(LOGIN_REQUEST);

        verify(applicationService).setState(LoginGetNameState.class);
    }

    @Test
    void processInputAudit() {
        state.processInput(AUDIT_REQUEST);

        verify(applicationService).printAudit();
    }
}