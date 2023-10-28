package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.BalanceResponse;
import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.exception.ResourceProcessingException;
import io.ylab.wallet.domain.port.output.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private static final BigDecimal BALANCE = BigDecimal.valueOf(99.99);
    private static final Account ACCOUNT = Account.builder()
            .balance(BALANCE)
            .build();
    private static final long USER_ID = 1L;

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final AccountService accountService = new AccountService(accountRepository);

    @Test
    void getBalance() {
        when(accountRepository.getByUserId(USER_ID)).thenReturn(Optional.of(ACCOUNT));
        BalanceResponse expectedBalance = new BalanceResponse(BALANCE.toString());
        BalanceResponse actualBalance = accountService.getBalance(USER_ID);

        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("getBalance should throw exception if user not exists")
    void getBalanceIfUserNotExists() {
        when(accountRepository.getByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getBalance(USER_ID))
                .isInstanceOf(ResourceProcessingException.class)
                .hasMessage("Не удалось загрузить баланс!");
    }

    @Test
    void updateAccount() {
        accountService.updateAccountBalance(ACCOUNT);

        verify(accountRepository).updateBalance(ACCOUNT);
    }
}
