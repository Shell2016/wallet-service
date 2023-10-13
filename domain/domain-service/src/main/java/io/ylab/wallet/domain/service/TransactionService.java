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
    private final TransactionRepository transactionRepository;
    /**
     * Injection of service that contains user business logic.
     */
    private final UserService userService;
    /**
     * Injection of service that contains account business logic.
     */
    private final AccountService accountService;

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
        return transactionRepository.exists(id);
    }

    /**
     * Gets list of current user's transactions.
     * @param userId of current user
     * @return list of current user's transactions
     */
    public List<Transaction> getUserTransactions(long userId) {
        return transactionRepository.getAllByUserId(userId);
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
                                      long userId,
                                      TransactionType type,
                                      String amount) {
        if (transactionExists(transactionId)) {
            throw new TransactionException("Транзакция с id=" + transactionId + " уже зарегистрирована в системе!\n" +
                    "Операция отклонена!");
        }
        User user = userService.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь не найден!"));
        Account account = user.getAccount();
        if (TransactionType.DEPOSIT == type) {
            account.deposit(new BigDecimal(amount));
        } else if (TransactionType.WITHDRAW == type) {
            account.withdraw(new BigDecimal(amount));
        }
        Transaction transaction = null;
        if (accountService.updateAccountBalance(account)) {
            transaction = saveTransaction(transactionId, userId, type, amount);
        }
        return transaction;
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
                                        long userId,
                                        TransactionType type,
                                        String amount) {
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .id(UUID.fromString(transactionId))
                .userId(userId)
                .type(type)
                .amount(new BigDecimal(amount))
                .createdAt(LocalDateTime.now())
                .build());
        System.out.println("Транзакция " + transactionId + " зарегистрирована в системе!");
        return transaction;
    }
}
