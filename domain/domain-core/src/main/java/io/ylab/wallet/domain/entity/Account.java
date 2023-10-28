package io.ylab.wallet.domain.entity;

import io.ylab.wallet.domain.exception.BalanceValidationException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Builder
public class Account {

    private final long id;
    /**
     * Id of the owner of the account.
     */
    private final User user;

    private BigDecimal balance;

    public void setBalance(BigDecimal balance) {
        this.balance = setScale(balance);
    }

    public BigDecimal deposit(BigDecimal amount) {
        this.balance = setScale(balance.add(amount));
        return this.balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        validateWithdrawal(amount);
        this.balance = setScale(balance.subtract(amount));
        return this.balance;
    }

    private void validateWithdrawal(BigDecimal amount) {
        if (!isGreaterThanOrEqual(amount)) {
            throw new BalanceValidationException("Недостаточно средств на счете!");
        }
    }

    private boolean isGreaterThanOrEqual(BigDecimal amount) {
        return this.balance != null && this.balance.compareTo(amount) >= 0;
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
