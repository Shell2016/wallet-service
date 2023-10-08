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

@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final UserService userService;

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public boolean transactionExists(String id) {
        return repository.exists(id);
    }

    public List<Transaction> getUserTransactions(String userId) {
        return repository.getAll().stream()
                .filter(transaction -> transaction.getUserId().toString().equals(userId))
                .toList();
    }

    public Account processTransaction(String transactionId,
                                      String userId,
                                      TransactionType type,
                                      String amount) {
        if (transactionExists(transactionId)) {
            throw new TransactionException("Транзакция с данным идентификатором уже зарегистрирована в системе!");
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
        saveTransaction(transactionId, userId, type, amount);
        return account;
    }

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
