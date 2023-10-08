package io.ylab.wallet.database.storage;


import io.ylab.wallet.domain.entity.Transaction;

import java.util.*;

public class InMemoryTransactionStorage {
    private final Map<String, Transaction> transactions = new LinkedHashMap<>();

    public Transaction save(Transaction transaction) {
        return transactions.put(transaction.getId().toString(), transaction) == null ? transaction : null;
    }

    public boolean exists(String id) {
        return transactions.get(id) != null;
    }

    public List<Transaction> getAll() {
        return transactions.values().stream()
                .toList();
    }
}
