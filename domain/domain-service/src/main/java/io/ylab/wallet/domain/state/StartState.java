package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

/**
 * Initial state when program starts or user is not authenticated.
 * While system in this state - State.getContext() returns userName required for registration
 */
public class StartState extends State {

    public StartState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows initial menu.
     */
    @Override
    public void showMenu() {
        System.out.println("""
                Для завершения программы введите: exit
                
                Для выбора действия введите нужное число в консоль:
                1. Логин
                2. Регистрация нового пользователя
                """);
    }

    /**
     * Processing user input.
     */
    public void processInput(String input) {
        if ("2".equals(input)) {
            app.setState(RegistrationGetNameState.class);
        } else if ("1".equals(input)) {
            app.setState(LoginGetNameState.class);
        } else if ("getAudit".equals(input)) {
            app.printAudit();
        }
    }
}
