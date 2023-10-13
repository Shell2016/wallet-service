package io.ylab.wallet.entity;

import lombok.*;

import java.math.BigDecimal;

/**
 * Data access entity.
 */
@Getter
@Setter
@ToString
@Builder
public class AccountEntity {

    private long id;
    private UserEntity user;
    private BigDecimal balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity account = (AccountEntity) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
