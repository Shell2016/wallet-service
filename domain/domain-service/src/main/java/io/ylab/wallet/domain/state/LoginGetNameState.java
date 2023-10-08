package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

public class LoginGetNameState extends State {

    public static final String USERNAME_EMPTY_ERROR_MESSAGE = "Поле имя не должно быть пустым!";

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
            app.audit("Login error: " + USERNAME_EMPTY_ERROR_MESSAGE);
            throw new ValidationException(USERNAME_EMPTY_ERROR_MESSAGE);
        }
        setContext(userName);
        app.setState(LoginGetPasswordState.class);
        return userName;
    }
}
