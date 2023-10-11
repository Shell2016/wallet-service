package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.exception.InvalidCredentialsException;
import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginGetPasswordStateTest {

    private static final String USERNAME = "test1";
    private static final String PASSWORD = "123456";
    private static final String USER_ID = "0ff72a9a-a7a3-4c17-a25e-b5ef5215d528";
    private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "Пользователя с такими именем и паролем не найдено!";
    private static final UserDto USER_DTO = UserDto.builder()
            .id(UUID.fromString(USER_ID))
            .account(new Account(UUID.fromString(USER_ID)))
            .build();

    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new LoginGetPasswordState(applicationService);

    @BeforeEach
    void init() {
        State.setContext(USERNAME);
    }

    @Test
    void processInputGetPassword() {
        when(applicationService.getUserIfValidCredentials(USERNAME, PASSWORD))
                .thenReturn(Optional.of(USER_DTO));

        state.processInput(PASSWORD);

        assertThat(State.getContext()).isEqualTo(USER_ID);
        verify(applicationService).setState(AuthorizedState.class);
    }

    @Test
    void processInputGetPasswordInvalidCredentials() {
        when(applicationService.getUserIfValidCredentials(USERNAME, PASSWORD))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> state.processInput(PASSWORD))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasNoCause()
                .hasMessage(INVALID_CREDENTIALS_ERROR_MESSAGE);

        assertThat(State.getContext()).isNull();
        verify(applicationService).setState(StartState.class);
        verify(applicationService).audit("Login error: " + INVALID_CREDENTIALS_ERROR_MESSAGE);
    }
}