package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for managing first registration phase, processing user name.
 * While system in this state - State.getContext() == null
 */
public class RegistrationGetNameState extends State {

    public static final String EMPTY_STRING_ERROR_MESSAGE = "Поле имя не должно быть пустым!";

    public RegistrationGetNameState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows user prompt for username.
     */
    @Override
    public void showMenu() {
        System.out.println("Введите имя:");
    }

    /**
     * Processing username.
     */
    @Override
    public void processInput(String input) {
        String userName = input.trim();
        if (userName.isEmpty()) {
            app.audit("Registration error: " + EMPTY_STRING_ERROR_MESSAGE);
            throw new ValidationException(EMPTY_STRING_ERROR_MESSAGE);
        }
        setContext(userName);
        app.setState(RegistrationGetPasswordState.class);
    }
}
