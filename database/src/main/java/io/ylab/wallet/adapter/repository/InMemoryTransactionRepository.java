package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.InMemoryTransactionStorage;
import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * In-memory implementation of TransactionRepository.
 */
@RequiredArgsConstructor
public class InMemoryTransactionRepository implements TransactionRepository {

    /**
     * Data storage for transactions.
     */
    private final InMemoryTransactionStorage storage;

    /**
     * Checks if transaction exists
     * @param id of transaction
     * @return true if transaction exists
     */
    @Override
    public boolean exists(String id) {
        return storage.exists(id);
    }

    /**
     * Saves transaction.
     * @param transaction to save
     * @return saved transaction
     */
    @Override
    public Transaction save(Transaction transaction) {
        return storage.save(transaction);
    }

    /**
     * Get list of all transactions.
     * @return list of transactions
     */
    @Override
    public List<Transaction> getAll() {
        return storage.getAll();
    }
}
