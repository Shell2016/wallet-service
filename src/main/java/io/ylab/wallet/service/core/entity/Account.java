package io.ylab.wallet.service.core.entity;

import io.ylab.wallet.service.core.exception.TransactionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {

    private BigDecimal balance = setScale(BigDecimal.ZERO);

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
}
