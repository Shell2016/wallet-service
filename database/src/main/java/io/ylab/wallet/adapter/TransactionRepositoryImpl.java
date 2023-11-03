package io.ylab.wallet.adapter;

import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.domain.port.output.repository.TransactionRepository;
import io.ylab.wallet.mapper.TransactionDataAccessMapper;
import io.ylab.wallet.repository.JdbcTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adapter between TransactionRepository and JdbcTransactionRepository.
 */
@RequiredArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    /**
     * Concrete repository that responsible for processing transaction data.
     */
    private final JdbcTransactionRepository jdbcTransactionRepository;
    /**
     * Maps between domain entities and data access entities.
     */
    private final TransactionDataAccessMapper transactionMapper;

    @Override
    public boolean exists(String id) {
        return jdbcTransactionRepository.exists(id);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionMapper.transactionEntityToTransaction(
                jdbcTransactionRepository.save(transactionMapper.transactionToTransactionEntity(transaction)));
    }

    @Override
    public List<Transaction> getAll() {
        return jdbcTransactionRepository.getAll().stream()
                .map(transactionMapper::transactionEntityToTransaction)
                .toList();
    }

    @Override
    public List<Transaction> getAllByUserId(long userId) {
        return jdbcTransactionRepository.getAllByUserId(userId).stream()
                .map(transactionMapper::transactionEntityToTransaction)
                .toList();
    }
}
