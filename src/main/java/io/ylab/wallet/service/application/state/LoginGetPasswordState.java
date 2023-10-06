package io.ylab.wallet.service.application.state;

import io.ylab.wallet.service.application.service.ApplicationService;
import io.ylab.wallet.service.core.exception.InvalidCredentialsException;

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
            State.clearContext();
            return PASSWORD_MASK;
        }
        String username = State.getContext();
        app.getUserIfValidCredentials(username, password).ifPresentOrElse(user -> {
                    State.setContext(user.id().toString());
                    app.setState(AuthorizedState.class);
                    System.out.println("Успешная авторизация!\n");
                },
                () -> {
                    State.clearContext();
                    app.setState(StartState.class);
                    throw new InvalidCredentialsException("Пользователя с такими именем и паролем не найдено!\n");
                });
        return PASSWORD_MASK;
    }
}
