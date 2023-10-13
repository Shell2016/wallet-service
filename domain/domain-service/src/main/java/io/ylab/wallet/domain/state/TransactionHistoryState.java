package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for showing transaction history of authenticated user.
 * While system in this state - State.getContext() returns current id of authenticated user.
 */
public class TransactionHistoryState extends State {
    public TransactionHistoryState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows transaction history of current user.
     */
    @Override
    public void showMenu() {
        System.out.println("\nИстория транзакций:\n");
        printTransactionHistory();
        System.out.println("\nНажмите Enter для выхода...\n");
    }

    /**
     * Exits on any user input.
     */
    @Override
    public void processInput(String input) {
        app.setState(AuthorizedState.class);
    }

    /**
     * Prints transaction history of current user.
     */
    private void printTransactionHistory() {
        long userId = Long.parseLong(State.getContext());
        app.getUserTransactions(userId).forEach(transaction -> {
            String id = transaction.getId().toString();
            String type = transaction.getType() == TransactionType.DEPOSIT ? "Пополнение" : "Снятие    ";
            String amount = transaction.getAmount().toString();
            String time = transaction.getCreatedAt().toString();
            System.out.printf("%s  %s  %s  %s%n", id, time, type, amount);
        });
    }
}
