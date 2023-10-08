package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.ApplicationService;

public class TransactionHistoryState extends State {
    public TransactionHistoryState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.println("\nИстория транзакций:\n");
        printTransactionHistory();
        System.out.println("\nНажмите Enter для выхода...\n");
    }

    @Override
    public String processRequest() {
        String input = app.getInput();
        app.setState(AuthorizedState.class);
        return input;
    }

    private void printTransactionHistory() {
        app.getUserTransactions().forEach(transaction -> {
            String id = transaction.getId().toString();
            String type = transaction.getType() == TransactionType.DEPOSIT ? "Пополнение" : "Снятие    ";
            String amount = transaction.getAmount().toString();
            String time = transaction.getCreatedAt().toString();
            System.out.printf("%s  %s  %s  %s%n", id, time, type, amount);
        });
    }
}
