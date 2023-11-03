package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.Transaction;
import io.ylab.wallet.entity.TransactionEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain entities and data access entities.
 */
@Mapper(componentModel = "spring")
public interface TransactionDataAccessMapper {

    TransactionEntity transactionToTransactionEntity(Transaction transaction);
    Transaction transactionEntityToTransaction(TransactionEntity transactionEntity);
}
