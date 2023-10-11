package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RegistrationGetPasswordStateTest {

    private static final String USERNAME = "test1";
    private static final String PASSWORD = "123456";
    private static final String USER_ID = "0ff72a9a-a7a3-4c17-a25e-b5ef5215d528";
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE =
            "Длина пароля должна быть не менее " + MIN_PASSWORD_LENGTH + " символов!";
    private static final UserDto USER_DTO = UserDto.builder()
            .id(UUID.fromString(USER_ID))
            .account(new Account(UUID.fromString(USER_ID)))
            .build();
    private static final String USER_EXIST_ERROR_MESSAGE = "Пользователь с таким именем уже существует!\n";

    private final ApplicationService applicationService = Mockito.mock(ApplicationService.class);
    private final State state = new RegistrationGetPasswordState(applicationService);

    @BeforeEach
    void init() {
        State.setContext(USERNAME);
    }

    @Test
    void processInputGetPassword() {
        when(applicationService.createUser(USERNAME, PASSWORD)).thenReturn(USER_DTO);

        state.processInput(PASSWORD);

        assertThat(State.getContext()).isEqualTo(USER_ID);
        verify(applicationService).setState(AuthorizedState.class);
        verify(applicationService).audit("Successful registration: userName=" + USERNAME + ", id=" + USER_ID);
    }

    @Test
    void processInputGetPasswordInvalidCredentials() {
        when(applicationService.createUser(USERNAME, PASSWORD))
                .thenThrow(new ResourceProcessingException(USER_EXIST_ERROR_MESSAGE));

        assertThatThrownBy(() -> state.processInput(PASSWORD))
                .isInstanceOf(ResourceProcessingException.class)
                .hasNoCause()
                .hasMessage(USER_EXIST_ERROR_MESSAGE);

        assertThat(State.getContext()).isNull();
        verify(applicationService).setState(StartState.class);
        verify(applicationService).audit("Registration error: userName=" + USERNAME + USER_EXIST_ERROR_MESSAGE);
    }

    @Test
    void processInputGetPasswordInvalidLength() {
        assertThatThrownBy(() -> state.processInput("123"))
                .isInstanceOf(ValidationException.class)
                .hasNoCause()
                .hasMessage(PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE);

        verify(applicationService).audit("Ошибка регистрации: " + PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE);
        verifyNoMoreInteractions(applicationService);
    }
}