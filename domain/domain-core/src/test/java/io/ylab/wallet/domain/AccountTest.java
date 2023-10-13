package io.ylab.wallet.domain;

import io.ylab.wallet.domain.entity.Account;
import io.ylab.wallet.domain.exception.BalanceValidationException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Account test")
class AccountTest {

    private static final BigDecimal AMOUNT_10000 = BigDecimal.valueOf(10000);
    private static final UUID UUID = java.util.UUID.fromString("adde1e02-1784-4973-956c-80d064309d55");
    private Account account;

    @BeforeEach
    void init() {
        account = new Account(UUID);
    }

    @Test
    @DisplayName("Check if new account balance is scaled zero")
    void checkIfNewAccountBalanceIsScaledZero() {
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    @DisplayName("Validation should pass")
    void validateWithdrawalShouldPass() {
        account.deposit(AMOUNT_10000);

        assertThatNoException().isThrownBy(() -> this.account.withdraw(BigDecimal.valueOf(5000)));
    }

    @Test
    @DisplayName("Validation should fail")
    void validateWithdrawalShouldFail() {
         account.deposit(AMOUNT_10000);

         assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(10001)))
                 .isInstanceOf(BalanceValidationException.class)
                 .hasMessage("Недостаточно средств на счете!");
    }

    @Test
    @DisplayName("Test successful withdrawal")
    void checkWithdrawal() {
        account.deposit(AMOUNT_10000);
        account.withdraw(BigDecimal.valueOf(5000.01));
        account.withdraw(BigDecimal.valueOf(1000));

        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(3999.99));
    }

    @Test
    @DisplayName("Test deposit")
    void checkDeposit() {
        account.deposit(AMOUNT_10000);
        account.deposit(AMOUNT_10000);

        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(20000).setScale(2, RoundingMode.HALF_EVEN));
    }
}
