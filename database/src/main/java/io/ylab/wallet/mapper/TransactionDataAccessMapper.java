package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.entity.TransactionEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between domain entities and data access entities.
 */
@Component
public class TransactionDataAccessMapper {

    public TransactionEntity transactionToTransactionEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .type(transaction.getType())
                .build();
    }

    public Transaction transactionEntityToTransaction(TransactionEntity transactionEntity) {
        return Transaction.builder()
                .id(transactionEntity.getId())
                .userId(transactionEntity.getUserId())
                .amount(transactionEntity.getAmount())
                .createdAt(transactionEntity.getCreatedAt())
                .type(transactionEntity.getType())
                .build();
    }
}
