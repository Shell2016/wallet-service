package io.ylab.wallet.adapter;

import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import io.ylab.wallet.mapper.AccountDataAccessMapper;
import io.ylab.wallet.repository.JdbcAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter between AccountRepository and JdbcAccountRepository.
 */
@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    /**
     * Concrete repository that responsible for processing account data.
     */
    private final JdbcAccountRepository jdbcAccountRepository;
    /**
     * Maps between domain entities and data access entities.
     */
    private final AccountDataAccessMapper accountMapper;

    @Override
    public Account save(Account account) {
        return accountMapper.accountEntityToAccount(
                jdbcAccountRepository.save(accountMapper.accountToAccountEntity(account)));
    }

    @Override
    public boolean updateBalance(Account account) {
        return jdbcAccountRepository.updateBalance(accountMapper.accountToAccountEntity(account));
    }

    @Override
    public Optional<Account> getByUserId(long userId) {
        return jdbcAccountRepository.getByUserId(userId)
                .map(accountMapper::accountEntityToAccount);
    }
}
