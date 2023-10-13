package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for managing first login phase, processing username.
 * While system in this state - State.getContext() == null
 */
public class LoginGetNameState extends State {

    public static final String USERNAME_EMPTY_ERROR_MESSAGE = "Поле имя не должно быть пустым!";

    public LoginGetNameState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows prompt for user to enter username.
     */
    @Override
    public void showMenu() {
        System.out.println("""
                Для завершения программы введите: exit
                
                Введите имя пользователя:
                """);
    }

    /**
     * Processing username.
     */
    @Override
    public void processInput(String input) {
        String userName = input.trim();
        if (userName.isEmpty()) {
            app.audit("Login error: " + USERNAME_EMPTY_ERROR_MESSAGE);
            throw new ValidationException(USERNAME_EMPTY_ERROR_MESSAGE);
        }
        setContext(userName);
        app.setState(LoginGetPasswordState.class);
    }
}
