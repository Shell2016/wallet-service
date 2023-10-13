package io.ylab.wallet.entity;

import io.ylab.wallet.domain.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data access entity.
 */
@Getter
@Setter
@ToString
@Builder
public class TransactionEntity {
    private UUID id;
    private long userId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionEntity that = (TransactionEntity) o;

        if (userId != that.userId) return false;
        if (!id.equals(that.id)) return false;
        if (!amount.equals(that.amount)) return false;
        if (type != that.type) return false;
        return createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + amount.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }
}
