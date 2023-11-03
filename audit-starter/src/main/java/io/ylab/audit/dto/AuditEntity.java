package io.ylab.audit.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data access audit entity.
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
