package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

public class WithdrawalState extends State {
    public WithdrawalState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.printf("""
                Для завершения программы введите: exit
                        
                Введите через пробел в одну строку уникальный идентификатор транзакции и сумму для списания, например:
                adde1e02-1784-4973-956c-80d064309d55 541.05
                Ваш идентификатор транзакции:
                %s
                %n""", app.generateTransactionId());
    }

    @Override
    public String processRequest() {
        app.getInput();
        return null;
    }
}
