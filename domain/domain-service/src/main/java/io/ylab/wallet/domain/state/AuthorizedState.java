package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

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
                clearContext();
                app.setState(StartState.class);
                System.out.println("Произведен выход из системы...\n");
            }
            case "1" -> {
                app.setState(ViewBalanceState.class);
            }
            case "2" -> {
                app.setState(DepositState.class);
            }
            case "3" -> {
                app.setState(WithdrawalState.class);
            }
            case "4" -> {
                app.setState(TransactionHistoryState.class);
            }
            default -> System.out.println("Неизвестная команда!\n");
        }
        return input;
    }
}
