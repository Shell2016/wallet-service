package io.ylab.wallet.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.ylab.wallet.domain.entity.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dto for transaction representation.
 * @param id
 * @param userId
 * @param amount
 * @param type
 * @param createdAt
 */
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDto(UUID id,
                             long userId,
                             BigDecimal amount,
                             TransactionType type,
                             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                             LocalDateTime createdAt) {
}
