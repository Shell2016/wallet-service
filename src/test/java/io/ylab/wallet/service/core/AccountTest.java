package io.ylab.wallet.service.core;

import io.ylab.wallet.service.core.entity.Account;
import io.ylab.wallet.service.core.exception.TransactionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    public static final BigDecimal AMOUNT_10000 = BigDecimal.valueOf(10000);
    private Account account;

    @BeforeEach
    void init() {
        account = new Account();
    }

    @Test
    void checkIfNewAccountBalanceIsScaledZero() {
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void validateWithdrawalShouldPass() {
        account.deposit(AMOUNT_10000);

        assertThatNoException().isThrownBy(() -> this.account.withdraw(BigDecimal.valueOf(5000)));
    }

    @Test
    void validateWithdrawalShouldFail() {
         account.deposit(AMOUNT_10000);

         assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(10001)))
                 .isInstanceOf(TransactionException.class)
                 .hasMessage("Недостаточно средств на счете!");
    }

    @Test
    void checkWithdrawal() {
        account.deposit(AMOUNT_10000);
        account.withdraw(BigDecimal.valueOf(5000.01));
        account.withdraw(BigDecimal.valueOf(1000));

        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(3999.99));
    }

    @Test
    void checkDeposit() {
        account.deposit(AMOUNT_10000);
        account.deposit(AMOUNT_10000);

        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(20000).setScale(2, RoundingMode.HALF_EVEN));
    }

}
