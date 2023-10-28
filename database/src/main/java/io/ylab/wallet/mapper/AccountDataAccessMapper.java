package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.entity.AccountEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between domain entities and data access entities.
 */
@Component
public class AccountDataAccessMapper {

    public Account accountEntityToAccount(AccountEntity accountEntity) {
        return Account.builder()
                .id(accountEntity.getId())
                .balance(accountEntity.getBalance())
                .build();
    }

    public AccountEntity accountToAccountEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .build();
    }
}
