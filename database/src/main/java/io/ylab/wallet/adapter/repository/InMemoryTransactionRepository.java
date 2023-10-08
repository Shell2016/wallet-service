package io.ylab.wallet.adapter.repository;

import io.ylab.wallet.database.storage.transaction.InMemoryTransactionStorage;
import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InMemoryTransactionRepository implements TransactionRepository {

    private final InMemoryTransactionStorage storage;

    @Override
    public boolean exists(String id) {
        return storage.exists(id);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return storage.save(transaction);
    }

    @Override
    public List<Transaction> getAll() {
        return storage.getAll();
    }
}
