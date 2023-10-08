package io.ylab.wallet.domain.state;


import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.exception.ValidationException;
import io.ylab.wallet.domain.service.ApplicationService;

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
            clearContext();
            return PASSWORD_MASK;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Длина пароля должна быть не менее " + MIN_PASSWORD_LENGTH + "  символов!");
        }
        String username = getContextAndClear();
        UserDto user;
        try {
            user = app.createUser(username, password);
        } catch (ResourceProcessingException e) {
            app.setState(StartState.class);
            clearContext();
            throw e;
        }
        setContext(user.id().toString());
        app.setState(AuthorizedState.class);
        return PASSWORD_MASK;
    }
}
