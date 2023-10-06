package io.ylab.wallet.service.application.state;

import io.ylab.wallet.service.application.dto.UserDto;
import io.ylab.wallet.service.application.service.ApplicationService;
import io.ylab.wallet.service.core.exception.ResourceProcessingException;
import io.ylab.wallet.service.core.exception.ValidationException;

public class RegistrationGetPasswordState extends State {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final String PASSWORD_MASK = "********";

    public RegistrationGetPasswordState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("Введите пароль (минимум " + MIN_PASSWORD_LENGTH + " символов) или 'esc' для отмены");
    }

    @Override
    public String processRequest() {
        String password = app.getInput().trim();
        if ("esc".equals(password)) {
            app.setState(StartState.class);
            State.clearContext();
            return PASSWORD_MASK;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Длина пароля должна быть не менее " + MIN_PASSWORD_LENGTH + "  символов!");
        }
        String username = State.getContext();
        UserDto user;
        try {
            user = app.createUser(username, password);
        } catch (ResourceProcessingException e) {
            app.setState(StartState.class);
            State.clearContext();
            throw e;
        }
        State.setContext(user.id().toString());
        app.setState(AuthorizedState.class);
        return PASSWORD_MASK;
    }
}
