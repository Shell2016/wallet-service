package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.InvalidCredentialsException;
import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for managing second login phase, processing user password.
 * While system in this state - State.getContext() returns userName required for login
 */
public class LoginGetPasswordState extends State {

    public static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "Пользователя с такими именем и паролем не найдено!";

    public LoginGetPasswordState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows user prompt for password.
     */
    @Override
    public void showMenu() {
        System.out.println("Введите пароль или 'esc' для отмены");
    }

    /**
     * Processes username and password.
     */
    @Override
    public void processInput(String input) {
        String password = input.trim();
        if ("esc".equals(password)) {
            app.setState(StartState.class);
            clearContext();
        }
        String username = getContextAndClear();
        app.getUserIfValidCredentials(username, password).ifPresentOrElse(user -> {
                    setContext(user.id().toString());
                    app.setState(AuthorizedState.class);
                    System.out.println("Успешная авторизация!\n");
                    app.audit("Successful login: username=" + username + ", userId=" + user.id());
                },
                () -> {
                    clearContext();
                    app.setState(StartState.class);
                    app.audit("Login error: " + INVALID_CREDENTIALS_ERROR_MESSAGE);
                    throw new InvalidCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
                });
    }
}
