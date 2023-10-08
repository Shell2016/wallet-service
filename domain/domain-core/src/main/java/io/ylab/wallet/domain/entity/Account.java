package io.ylab.wallet.domain.entity;

import io.ylab.wallet.domain.exception.TransactionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Account {

    private final UUID userId;

    private BigDecimal balance = setScale(BigDecimal.ZERO);

    public Account(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal deposit(BigDecimal amount) {
        balance = setScale(balance.add(amount));
        System.out.println("Успешное пополнение счета!\nТекущий баланс: " + balance.toString());
        return balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        validateWithdrawal(amount);
        balance = setScale(balance.subtract(amount));
        System.out.println("Успешное снятие со счета!\nТекущий баланс: " + balance.toString());
        return balance;
    }

    private void validateWithdrawal(BigDecimal amount) {
        if (!isGreaterThanOrEqual(amount)) {
            throw new TransactionException("Недостаточно средств на счете!");
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

        if (!userId.equals(account.userId)) return false;
        return balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }
}
