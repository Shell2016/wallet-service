package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for managing requests from authorized users.
 * While system in this state - State.getContext() will return userId of authorized user.
 */
public class AuthorizedState extends State {
    public AuthorizedState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows user menu to authorized user.
     */
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

    /**
     * Processes requests from authorized user.
     */
    @Override
    public void processInput(String input) {
        switch (input) {
            case "5" -> {
                String userId = getContext();
                clearContext();
                app.setState(StartState.class);
                System.out.println("Произведен выход из системы...\n");
                app.audit("Successful logout: userId=" + userId);
            }
            case "1" -> {
                app.setState(ViewBalanceState.class);
                app.audit("Balance request from user with id: " + State.getContext());
            }
            case "2" -> {
                app.setState(DepositState.class);
                app.audit("Initiated deposit transaction by user with id " + State.getContext());
            }
            case "3" -> {
                app.setState(WithdrawalState.class);
                app.audit("Initiated withdrawal transaction by user with id " + State.getContext());
            }
            case "4" -> {
                app.setState(TransactionHistoryState.class);
                app.audit("Requested transaction history by user with id " + State.getContext());
            }
            default -> System.out.println("Неизвестная команда!\n");
        }
    }
}
