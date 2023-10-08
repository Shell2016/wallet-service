package io.ylab.wallet.domain.entity;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class Transaction {
    private final UUID id;
    private BigDecimal amount;
    private TransactionType type;
}
