package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class RegistrationGetNameStateTest {

    public static final String USERNAME = "test1";
    public static final String USERNAME_EMPTY_ERROR_MESSAGE = "Поле имя не должно быть пустым!";

    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new RegistrationGetNameState(applicationService);

    @BeforeEach
    void init() {
        State.clearContext();
    }

    @Test
    void processInputRegistrationName() {
        state.processInput(USERNAME);

        assertThat(State.getContext()).isEqualTo(USERNAME);
        verify(applicationService).setState(RegistrationGetPasswordState.class);
    }

    @Test
    void processInputRegistrationNameEmptyInput() {
        assertThatThrownBy(() -> state.processInput("  "))
                .hasNoCause()
                .hasMessage(USERNAME_EMPTY_ERROR_MESSAGE)
                .isInstanceOf(ValidationException.class);

        assertThat(State.getContext()).isNull();
        verify(applicationService).audit("Registration error: " + USERNAME_EMPTY_ERROR_MESSAGE);
        verifyNoMoreInteractions(applicationService);
    }
}