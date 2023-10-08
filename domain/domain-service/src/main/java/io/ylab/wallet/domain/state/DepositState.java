package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.entity.TransactionType;
import io.ylab.wallet.domain.service.ApplicationService;
import io.ylab.wallet.domain.service.TransactionUtils;

public class DepositState extends State {
    public DepositState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.printf("""
                Для завершения программы введите: exit
                        
                Введите через пробел в одну строку уникальный идентификатор транзакции и сумму для пополнения, например:
                adde1e02-1784-4973-956c-80d064309d55 541.05
                Ваш идентификатор транзакции:
                %s
                %n""", app.generateTransactionId());
    }

    @Override
    public String processRequest() {
        String input = app.getInput();
        String[] inputArray = TransactionUtils.processTransactionInput(input);
        String transactionId = inputArray[0];
        String amount = inputArray[1];
        app.processTransaction(transactionId, TransactionType.DEPOSIT, amount);
        app.setState(AuthorizedState.class);
        return input;
    }
}
