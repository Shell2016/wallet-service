package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

/**
 * State for showing balance of authenticated user.
 * While system in this state - State.getContext() returns current id of authenticated user.
 */
public class ViewBalanceState extends State {
    public ViewBalanceState(ApplicationService app) {
        super(app);
    }

    /**
     * Shows balance of current user.
     */
    @Override
    public void showMenu() {
        System.out.printf("""
                Для завершения программы введите: exit
                        
                Текущий баланс: %s
                        
                Нажмите Enter для выхода...
                %n""", app.getBalance());
    }

    /**
     * Exits on any user input.
     */
    @Override
    public void processInput(String input) {
        app.setState(AuthorizedState.class);
    }
}
