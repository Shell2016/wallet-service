package io.ylab.wallet.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Transaction extends BaseEntity<UUID> {
    private BigDecimal amount;
    private TransactionType type;
}
