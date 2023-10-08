package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

public class StartState extends State {

    public StartState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("""
                Для завершения программы введите: exit
                
                Для выбора действия введите нужное число в консоль:
                1. Логин
                2. Регистрация нового пользователя
                """);
    }

    public String processRequest() {
        String input = app.getInput();
        if ("2".equals(input)) {
            app.setState(RegistrationGetNameState.class);
        } else if ("1".equals(input)) {
            app.setState(LoginGetNameState.class);
        } else if ("getAudit".equals(input)) {
            app.printAudit();
        }
        return input;
    }




}
