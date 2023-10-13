package io.ylab.wallet.domain.port.output.repository;

import io.ylab.wallet.domain.entity.Account;

import java.util.Optional;

/**
 * Interface for interacting with account table.
 * Acts as output port in onion architecture.
 */
public interface AccountRepository {
    Account save(Account account);
    boolean updateBalance(Account account);
    Optional<Account> getByUserId(long userId);
}
