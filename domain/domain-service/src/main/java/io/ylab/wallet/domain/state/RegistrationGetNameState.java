package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;


public class RegistrationGetNameState extends State {

    public static final String EMPTY_STRING_ERROR_MESSAGE = "Поле имя не должно быть пустым!";

    public RegistrationGetNameState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("Введите имя:");
    }

    @Override
    public String processRequest() {
        String userName = app.getInput().trim();
        if (userName.isEmpty()) {
            app.audit("Registration error: " + EMPTY_STRING_ERROR_MESSAGE);
            throw new ValidationException(EMPTY_STRING_ERROR_MESSAGE);
        }
        setContext(userName);
        app.setState(RegistrationGetPasswordState.class);
        return userName;
    }
}
