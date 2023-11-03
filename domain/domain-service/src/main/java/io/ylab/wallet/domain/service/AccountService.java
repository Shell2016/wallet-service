package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.BalanceResponse;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.entity.User;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service that contains account business logic.
 */
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Gets current user's balance.
     *
     * @return balanceResponse
     */
    public BalanceResponse getBalance(long userId) {
        return accountRepository.getByUserId(userId)
                .map(account -> new BalanceResponse(account.getBalance().toString()))
                .orElseThrow(() -> new ResourceProcessingException("Cannot load balance!"));
    }

    /**
     * Updates given account.
     *
     * @param account to update
     * @return true if successful update
     */
    public boolean updateAccountBalance(Account account) {
        return accountRepository.updateBalance(account);
    }

    /**
     * Builds new account and passes it to repository for creation.
     *
     * @param user that is set in new account
     * @return created Account
     */
    public Account save(User user) {
        return accountRepository.save(Account.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build());
    }

    /**
     * Gets account by user id
     * @param id of the user
     * @return found account of empty optional
     */
    public Optional<Account> getByUserId(long id) {
        return accountRepository.getByUserId(id);
    }
}
