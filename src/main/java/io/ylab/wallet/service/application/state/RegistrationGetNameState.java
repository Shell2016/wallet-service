package io.ylab.wallet.service.application.state;

import io.ylab.wallet.service.application.service.ApplicationService;
import io.ylab.wallet.service.core.exception.ValidationException;

public class RegistrationGetNameState extends State {

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
            throw new ValidationException("Поле не должно быть пустым!");
        }
        State.setContext(userName);
        app.setState(RegistrationGetPasswordState.class);
        return userName;
    }
}
