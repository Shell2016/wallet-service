package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;


public class ViewBalanceState extends State {
    public ViewBalanceState(ApplicationService app) {
        super(app);
    }

    @Override
    public void showMenu() {
        System.out.printf("""
                Для завершения программы введите: exit
                        
                Текущий баланс: %s
                        
                Нажмите Enter для выхода...
                %n""", app.getBalance());
    }

    @Override
    public String processRequest() {
        String input = app.getInput();
        app.setState(AuthorizedState.class);
        return input;
    }
}
