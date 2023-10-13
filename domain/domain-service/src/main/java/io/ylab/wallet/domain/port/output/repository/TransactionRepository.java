package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.entity.Transaction;

import java.util.List;

/**
 * Interface for interacting with transaction data storage.
 * Acts as output port in onion architecture.
 */
public interface TransactionRepository {
    boolean exists(String id);
    Transaction save(Transaction transaction);
    List<Transaction> getAll();
    List<Transaction> getAllByUserId(long userId);
}
