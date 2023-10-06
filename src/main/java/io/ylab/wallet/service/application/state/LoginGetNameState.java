package io.ylab.wallet.service.application.state;

import io.ylab.wallet.service.application.service.ApplicationService;
import io.ylab.wallet.service.core.exception.ValidationException;

public class LoginGetNameState extends State {
    public LoginGetNameState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("""
                Для завершения программы введите: exit
                Введите имя пользователя:
                """);

    }

    @Override
    public String processRequest() {
        String userName = app.getInput().trim();
        if (userName.isEmpty()) {
            throw new ValidationException("Поле не должно быть пустым!");
        }
        State.setContext(userName);
        app.setState(LoginGetPasswordState.class);
        return userName;
    }


}
