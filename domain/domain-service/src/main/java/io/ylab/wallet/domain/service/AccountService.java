package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service that contains account business logic.
 */
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Gets current user's balance.
     * @return account balance as string
     */
    public String getBalance(long userId) {
        return accountRepository.getByUserId(userId)
                .map(account -> account.getBalance().toString())
                .orElseThrow(() -> new ResourceProcessingException("Не удалось загрузить баланс!"));
    }

    /**
     * Updates given account.
     * @param account to update
     * @return true if successful update
     */
    public boolean updateAccountBalance(Account account) {
        return accountRepository.updateBalance(account);
    }

}
