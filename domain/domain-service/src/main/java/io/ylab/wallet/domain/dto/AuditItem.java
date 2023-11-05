package io.ylab.wallet.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Class for storing audit info.
 */
@Builder
@Getter
@ToString
public class AuditItem {
    private long id;
    private LocalDateTime createdAt;
    private String info;
}
