package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.entity.AccountEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain entities and data access entities.
 */
@Mapper(componentModel = "spring")
public interface AccountDataAccessMapper {

    Account accountEntityToAccount(AccountEntity accountEntity);
    AccountEntity accountToAccountEntity(Account account);
}
