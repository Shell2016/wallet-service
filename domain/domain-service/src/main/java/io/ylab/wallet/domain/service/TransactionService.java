package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.TransactionDto;
import io.ylab.wallet.domain.dto.TransactionRequest;
import io.ylab.wallet.domain.entity.*;
import io.ylab.wallet.domain.exception.*;
import io.ylab.wallet.domain.mapper.TransactionMapper;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service that contains transaction business logic.
 */
@RequiredArgsConstructor
@Service
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
     * Mapper for mapping transaction dtos.
     */
    private final TransactionMapper transactionMapper;

    /**
     * Checks if transaction with given id already exists.
     *
     * @param id of transaction to check
     * @return true if transaction exists in database
     */
    public boolean transactionExists(String id) {
        return transactionRepository.exists(id);
    }

    /**
     * Gets list of current user's transactions.
     *
     * @param userId of current user
     * @return list of current user's transactions
     */
    public List<TransactionDto> getUserTransactions(long userId) {
        return transactionRepository.getAllByUserId(userId).stream()
                .map(transactionMapper::map)
                .toList();
    }

    /**
     * Processes transaction.
     *
     * @param request transactionRequest with id, type and amount fields
     * @param userId  of corresponding user
     * @return Transaction entity
     */
    @Transactional
    public TransactionDto processTransaction(TransactionRequest request, long userId) {
        if (transactionExists(request.id())) {
            throw new TransactionException(
                    "Transaction with id=" + request.id() + " already registered! Operation cancelled!");
        }
        User user = userService.getUserById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found!"));
        Account account = user.getAccount();
        if (TransactionType.DEPOSIT.name().equalsIgnoreCase(request.type())) {
            account.deposit(new BigDecimal(request.amount()));
        } else if (TransactionType.WITHDRAW.name().equalsIgnoreCase(request.type())) {
            account.withdraw(new BigDecimal(request.amount()));
        }
        Transaction transaction = null;
        if (accountService.updateAccountBalance(account)) {
            transaction = saveTransaction(
                    request.id(),
                    userId,
                    TransactionType.valueOf(request.type().toUpperCase()),
                    request.amount());
        }
        validateAccountBalance(account.getBalance(), userId);
        return transactionMapper.map(transaction);
    }

    /**
     * Validates new account balance by comparing current balance and total sum from all transactions
     * of current user.
     *
     * @param balance new account balance to validate
     * @param userId  id of the current user
     */
    private void validateAccountBalance(BigDecimal balance, long userId) {
        BigDecimal amountByTransactionHistory = getAmountByTransactionHistory(userId);
        if (!Objects.equals(balance, amountByTransactionHistory)) {
            throw new BalanceValidationException("Account balance not in consistent state!");
        }
    }

    /**
     * Gets total sum of all user's transactions.
     *
     * @param userId id of the user
     * @return total sum of transactions
     */
    private BigDecimal getAmountByTransactionHistory(long userId) {
        return getUserTransactions(userId).stream()
                .map(transactionDto -> transactionDto.type() == TransactionType.DEPOSIT
                        ? transactionDto.amount()
                        : transactionDto.amount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Saves transaction into the database.
     *
     * @param transactionId of new transaction
     * @param userId        of current user
     * @param type          of transaction (deposit/withdraw)
     * @param amount        of money
     * @return Transaction
     */
    private Transaction saveTransaction(String transactionId,
                                        long userId,
                                        TransactionType type,
                                        String amount) {
        return transactionRepository.save(Transaction.builder()
                .id(UUID.fromString(transactionId))
                .userId(userId)
                .type(type)
                .amount(new BigDecimal(amount))
                .createdAt(LocalDateTime.now())
                .build());
    }
}
