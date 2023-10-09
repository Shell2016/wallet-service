package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.entity.*;
import io.ylab.wallet.domain.exception.TransactionException;
import io.ylab.wallet.domain.exception.UserNotFoundException;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service that contains transaction business logic.
 */
@RequiredArgsConstructor
public class TransactionService {

    /**
     * Injection of repository to persist transaction data.
     */
    private final TransactionRepository repository;
    /**
     * Injection of service that contains user business logic.
     */
    private final UserService userService;

    /**
     * Generates random UUID
     * @return new UUID as string
     */
    public String generateId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Checks if transaction with given id already exists.
     * @param id of transaction to check
     * @return true if transaction exists in database
     */
    public boolean transactionExists(String id) {
        return repository.exists(id);
    }

    /**
     * Gets list of current user's transactions.
     * @param userId of current user
     * @return list of current user's transactions
     */
    public List<Transaction> getUserTransactions(String userId) {
        return repository.getAll().stream()
                .filter(transaction -> transaction.getUserId().toString().equals(userId))
                .toList();
    }

    /**
     * Processes transaction.
     * @param transactionId of new transaction
     * @param userId of current user
     * @param type of transaction (deposit/withdraw)
     * @param amount of money
     * @return Transaction
     */
    public Transaction processTransaction(String transactionId,
                                      String userId,
                                      TransactionType type,
                                      String amount) {
        if (transactionExists(transactionId)) {
            throw new TransactionException("Транзакция с id=" + transactionId + " уже зарегистрирована в системе!");
        }
        User user = userService.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь не найден!"));
        Account account = user.getAccount();
        if (TransactionType.DEPOSIT == type) {
            account.deposit(new BigDecimal(amount));
        } else if (TransactionType.WITHDRAW == type) {
            account.withdraw(new BigDecimal(amount));
        }
        userService.updateUser(user);
        return saveTransaction(transactionId, userId, type, amount);
    }

    /**
     * Saves transaction.
     * @param transactionId of new transaction
     * @param userId of current user
     * @param type of transaction (deposit/withdraw)
     * @param amount of money
     * @return Transaction
     */
    private Transaction saveTransaction(String transactionId,
                                        String userId,
                                        TransactionType type,
                                        String amount) {
        Transaction transaction = repository.save(Transaction.builder()
                .id(UUID.fromString(transactionId))
                .userId(UUID.fromString(userId))
                .type(type)
                .amount(new BigDecimal(amount))
                .createdAt(LocalDateTime.now())
                .build());
        System.out.println("Транзакция " + transactionId + " зарегистрирована в системе!");
        return transaction;
    }
}
