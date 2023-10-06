package io.ylab.wallet.service.application.state;

import io.ylab.wallet.service.application.service.ApplicationService;

public class AuthorizedState extends State {

    public AuthorizedState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("""
                Для завершения программы введите: exit
                Для выбора действия введите нужное число в консоль:
                1. Просмотр текущего баланса
                2. Пополнить
                3. Снять
                4. Просмотр истории транзакций
                5. Выйти из системы
                """);
    }

    @Override
    public String processRequest() {
        String input = app.getInput();
        switch (input) {
            case "5" -> {
                State.clearContext();
                app.setState(StartState.class);
                System.out.println("Произведен выход из системы...\n");
            }
            case "1" -> {
                // TODO: 07.10.2023  
            }
            case "2" -> {
                // TODO: 07.10.2023  
            }
            case "3" -> {
                // TODO: 07.10.2023  
            }
            case "4" -> {
                // TODO: 07.10.2023
            }
            default -> System.out.println("Неизвестная операция!\n");
        }
        return input;
    }
}
