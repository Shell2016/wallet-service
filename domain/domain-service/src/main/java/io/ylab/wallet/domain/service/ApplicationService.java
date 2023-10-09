package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.UserDto;
import io.ylab.wallet.domain.entity.*;
import io.ylab.wallet.domain.exception.DomainException;
import io.ylab.wallet.domain.port.input.controller.WalletController;
import io.ylab.wallet.domain.state.*;

import java.util.*;

/**
 * Class that orchestrates all other systems of application.
 * It connects with infrastructure modules through interfaces(input/output ports).
 * Here implemented state pattern to get rid of massive switch statement.
 */
public class ApplicationService {
    /**
     * State that responsible for processing user request.
     */
    private State state;
    /**
     * Controller that gets user input.
     */
    private final WalletController controller;
    /**
     * Service that have user business logic.
     */
    private final UserService userService;
    /**
     * Service that have transaction business logic.
     */
    private final TransactionService transactionService;
    /**
     * Service that have audit business logic.
     */
    private final AuditService auditService;
    /**
     * Stores all possible states.
     */
    private final Map<Class<? extends State>, State> states = new HashMap<>();

    public ApplicationService(WalletController controller,
                              UserService userService,
                              TransactionService transactionService, AuditService auditService) {
        this.controller = controller;
        this.userService = userService;
        this.transactionService = transactionService;
        this.auditService = auditService;
        initializeStates();
        setState(StartState.class);
    }

    /**
     * Initializes states while creating instance of this class.
     */
    private void initializeStates() {
        states.put(StartState.class, new StartState(this));
        states.put(RegistrationGetNameState.class, new RegistrationGetNameState(this));
        states.put(RegistrationGetPasswordState.class, new RegistrationGetPasswordState(this));
        states.put(AuthorizedState.class, new AuthorizedState(this));
        states.put(LoginGetNameState.class, new LoginGetNameState(this));
        states.put(LoginGetPasswordState.class, new LoginGetPasswordState(this));
        states.put(ViewBalanceState.class, new ViewBalanceState(this));
        states.put(WithdrawalState.class, new WithdrawalState(this));
        states.put(DepositState.class, new DepositState(this));
        states.put(TransactionHistoryState.class, new TransactionHistoryState(this));
    }

    /**
     * Method for setting necessary state.
     * Invoked by State object.
     * @param clazz Class of state that system needs to be in the next step.
     */
    public void setState(Class<? extends State> clazz) {
        this.state = states.get(clazz);
    }

    /**
     * Gets current state
     * @return current state
     */
    public State getState() {
        return state;
    }

    /**
     * Main loop that runs user console.
     * Contains methods that transfers execution to current state.
     */
    public void run() {
        while (true) {
            showMenu();
            String input = null;
            try {
                input = processRequest();
            } catch (DomainException e) {
                System.out.println(e.getMessage());
            }
            if (exitRequestReceived(input)) {
                return;
            }
        }
    }

    /**
     * Shows menu.
     * Transfers execution to current state.
     */
    private void showMenu() {
        state.showMenu();
    }

    /**
     * Processes request and returns user input.
     * Transfers execution to current state.
     */
    private String processRequest() {
        return state.processRequest();
    }

    /**
     * Gets user input.
     * @return user input as string
     */
    public String getInput() {
        return controller.getInput();
    }

    /**
     * Checks if exit request received.
     * @param input string
     * @return true if input == "exit"
     */
    private boolean exitRequestReceived(String input) {
        return "exit".equals(input);
    }

    /**
     * Creates user.
     * @param username must be not empty
     * @param password length must be >= 6
     * @return UserDto of created user
     */
    public UserDto createUser(String username, String password) {
        return userService.createUser(username, password);
    }

    /**
     * Gets userDto if valid credentials.
     * @param username to login
     * @param password to login
     * @return Optional of userDto if valid credentials, empty Optional otherwise.
     */
    public Optional<UserDto> getUserIfValidCredentials(String username, String password) {
        return userService.getUserDtoIfValidCredentials(username, password);
    }

    /**
     * Gets current user's balance.
     * @return account balance
     */
    public String getBalance() {
        String userId = State.getContext();
        return userService.getBalance(userId);
    }

    /**
     * Generates random UUID.
     * @return new UUID as string
     */
    public String generateTransactionId() {
        return transactionService.generateId();
    }

    /**
     * Processes transaction.
     * @param transactionId that gives user
     * @param type of transaction - deposit or withdraw
     * @param amount of money
     * @return Transaction
     */
    public Transaction processTransaction(String transactionId, TransactionType type, String amount) {
        String userId = State.getContext();
        return transactionService.processTransaction(transactionId, userId, type, amount);
    }

    /**
     * Gets list of current user transactions.
     * @param userId of current user
     * @return list of transactions
     */
    public List<Transaction> getUserTransactions(String userId) {
        return transactionService.getUserTransactions(userId);
    }

    /**
     * Auditing some action.
     * @param info to audit
     */
    public void audit(String info) {
        auditService.audit(info);
    }

    /**
     * Prints audit logs to console.
     */
    public void printAudit() {
        auditService.getAuditInfo().forEach(System.out::println);
    }
}
