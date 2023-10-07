package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;


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
        setContext(userName);
        app.setState(RegistrationGetPasswordState.class);
        return userName;
    }
}
