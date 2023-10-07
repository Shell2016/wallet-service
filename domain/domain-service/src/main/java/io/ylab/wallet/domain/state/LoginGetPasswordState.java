package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.exception.InvalidCredentialsException;
import io.ylab.wallet.domain.service.ApplicationService;


public class LoginGetPasswordState extends State {

    public static final String PASSWORD_MASK = "********";

    public LoginGetPasswordState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("Введите пароль или 'esc' для отмены");
    }

    @Override
    public String processRequest() {
        String password = app.getInput().trim();
        if ("esc".equals(password)) {
            app.setState(StartState.class);
            clearContext();
            return PASSWORD_MASK;
        }
        String username = getContext();
        app.getUserIfValidCredentials(username, password).ifPresentOrElse(user -> {
                    setContext(user.id().toString());
                    app.setState(AuthorizedState.class);
                    System.out.println("Успешная авторизация!\n");
                },
                () -> {
                    clearContext();
                    app.setState(StartState.class);
                    throw new InvalidCredentialsException("Пользователя с такими именем и паролем не найдено!\n");
                });
        return PASSWORD_MASK;
    }
}
