package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.dto.UserResponse;
import io.ylab.wallet.domain.exception.InvalidCredentialsException;
import io.ylab.wallet.domain.service.ApplicationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginGetPasswordStateTest {

    private static final String USERNAME = "test1";
    private static final String PASSWORD = "123456";
    private static final long USER_ID = 1L;
    private static final long ACCOUNT_ID = 1L;
    private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "Пользователя с такими именем и паролем не найдено!";
    private static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(USER_ID)
            .username(USERNAME)
            .accountId(ACCOUNT_ID)
            .build();

    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new LoginGetPasswordState(applicationService);

    @BeforeEach
    void init() {
        State.setContext(USERNAME);
    }

    @Test
    void processInputGetPassword() {
        when(applicationService.getUserResponseIfValidCredentials(USERNAME, PASSWORD))
                .thenReturn(Optional.of(USER_RESPONSE));

        state.processInput(PASSWORD);

        assertThat(State.getContext()).isEqualTo(String.valueOf(USER_ID));
        verify(applicationService).setState(AuthorizedState.class);
    }

    @Test
    void processInputGetPasswordInvalidCredentials() {
        when(applicationService.getUserResponseIfValidCredentials(USERNAME, PASSWORD))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> state.processInput(PASSWORD))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasNoCause()
                .hasMessage(INVALID_CREDENTIALS_ERROR_MESSAGE);

        assertThat(State.getContext()).isNull();
        verify(applicationService).setState(StartState.class);
        verify(applicationService).audit("Login error: " + INVALID_CREDENTIALS_ERROR_MESSAGE);
    }
}