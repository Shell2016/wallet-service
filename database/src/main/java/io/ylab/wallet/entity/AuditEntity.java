package io.ylab.wallet.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data access entity.
 */
@Getter
@Setter
@ToString
@Builder
public class AuditEntity {

    private long id;
    private String info;
    private LocalDateTime createdAt;
}
