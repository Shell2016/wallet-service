package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.entity.Transaction;

import java.util.List;

public interface TransactionRepository {
    boolean exists(String id);
    Transaction save(Transaction transaction);
    List<Transaction> getAll();
}
