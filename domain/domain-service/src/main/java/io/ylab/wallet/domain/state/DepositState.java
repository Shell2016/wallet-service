package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.ApplicationService;
import io.ylab.wallet.domain.service.TransactionUtils;

/**
 * State for processing deposit transaction from authorized users.
 * While system in this state - State.getContext() will return userId of authorized user.
 */
public class DepositState extends State {
    public DepositState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows instructions to complete transaction.
     */
    @Override
    public void showMenu() {
        System.out.printf("""
                Для завершения программы введите: exit
                Для выхода из данного режима введите: esc
                        
                Введите через пробел в одну строку уникальный идентификатор транзакции и сумму для пополнения, например:
                adde1e02-1784-4973-956c-80d064309d55 541.05
                Ваш идентификатор транзакции:
                %s
                %n""", app.generateTransactionId());
    }

    /**
     * Processes transaction request.
     */
    @Override
    public void processInput(String input) {
        if ("esc".equals(input)) {
            app.setState(AuthorizedState.class);
            app.audit("Transaction cancelled by user " + State.getContext());
            return;
        }
        Transaction transaction;
        try {
            String[] inputArray = TransactionUtils.processTransactionInput(input);
            String transactionId = inputArray[0];
            String amount = inputArray[1];
            transaction = app.processTransaction(transactionId, TransactionType.DEPOSIT, amount);
        } catch (RuntimeException e) {
            app.audit("Transaction error: " + e.getMessage());
            throw e;
        }
        app.audit("Transaction success: " + transaction);
        app.setState(AuthorizedState.class);
    }
}
