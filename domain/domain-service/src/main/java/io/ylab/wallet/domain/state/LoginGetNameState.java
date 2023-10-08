package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

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
        setContext(userName);
        app.setState(LoginGetPasswordState.class);
        return userName;
    }
}
