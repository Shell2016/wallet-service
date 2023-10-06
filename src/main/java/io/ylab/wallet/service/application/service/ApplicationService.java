package io.ylab.wallet.service.application.service;

import io.ylab.wallet.service.application.controller.WalletController;
import io.ylab.wallet.service.application.dto.UserDto;
import io.ylab.wallet.service.application.state.*;
import io.ylab.wallet.service.core.exception.DomainException;

import java.util.*;

public class ApplicationService {
    private State state;
    private final WalletController controller;
    private final UserService userService;
    private final Map<Class<? extends State>, State> states = new HashMap<>();

    public ApplicationService(WalletController controller, UserService userService) {
        this.controller = controller;
        this.userService = userService;
        initializeStates();
        setState(StartState.class);
    }

    private void initializeStates() {
        states.put(StartState.class, new StartState(this));
        states.put(RegistrationGetNameState.class, new RegistrationGetNameState(this));
        states.put(RegistrationGetPasswordState.class, new RegistrationGetPasswordState(this));
        states.put(AuthorizedState.class, new AuthorizedState(this));
        states.put(LoginGetNameState.class, new LoginGetNameState(this));
        states.put(LoginGetPasswordState.class, new LoginGetPasswordState(this));
    }

    public void setState(Class<? extends State> clazz) {
        this.state = states.get(clazz);
    }

    public void run() {
        while (true) {
            showMenu();
            String input = null;
            try {
                input = processRequest();
            } catch (DomainException e) {
                System.out.println(e.getMessage());;
            }
            if (exitRequestReceived(input)) {
                return;
            }
        }
    }

    private void showMenu() {
        state.showMenu();
    }

    private String processRequest() {
        return state.processRequest();
    }

    public String getInput() {
        return controller.getInput();
    }

    private boolean exitRequestReceived(String input) {
        return "exit".equals(input);
    }

    public UserDto createUser(String username, String password) {
        return userService.createUser(username, password);
    }

    public Optional<UserDto> getUserIfValidCredentials(String username, String password) {
        return userService.getUserIfValidCredentials(username, password);
    }
}
