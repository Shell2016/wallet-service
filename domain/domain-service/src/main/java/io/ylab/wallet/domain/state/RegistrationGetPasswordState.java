package io.ylab.wallet.domain.state;


import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for managing second login phase, processing user password.
 * While system in this state - State.getContext() returns userName required for registration
 */
public class RegistrationGetPasswordState extends State {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final String PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE = "Длина пароля должна быть не менее " + MIN_PASSWORD_LENGTH + " символов!";
    public static final String PASSWORD_MASK = "********";

    public RegistrationGetPasswordState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows user prompt for password.
     */
    @Override
    public void showMenu() {
        System.out.println("Введите пароль (минимум " + MIN_PASSWORD_LENGTH + " символов) или 'esc' для отмены");
    }

    /**
     * Processing username and password.
     * @return user input
     */
    @Override
    public String processRequest() {
        String password = app.getInput().trim();
        if ("esc".equals(password)) {
            app.setState(StartState.class);
            clearContext();
            return PASSWORD_MASK;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            app.audit("Ошибка регистрации: " + PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE);
            throw new ValidationException(PASSWORD_LENGTH_VALIDATION_ERROR_MESSAGE);
        }
        String username = getContextAndClear();
        UserDto user;
        try {
            user = app.createUser(username, password);
        } catch (ResourceProcessingException e) {
            app.audit("Registration error: userName=" + username + e.getMessage());
            app.setState(StartState.class);
            clearContext();
            throw e;
        }
        setContext(user.id().toString());
        app.setState(AuthorizedState.class);
        app.audit("Successful registration: userName=" + username + ", id=" + user.id());
        return PASSWORD_MASK;
    }
}
