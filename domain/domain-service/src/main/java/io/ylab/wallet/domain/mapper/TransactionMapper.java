package io.ylab.wallet.domain.mapper;

import io.ylab.wallet.domain.dto.TransactionDto;
import io.ylab.wallet.domain.entity.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * Mapper for transaction - dto mapping.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction map(TransactionDto transactionDto);

    @InheritInverseConfiguration
    TransactionDto map(Transaction transaction);
}
