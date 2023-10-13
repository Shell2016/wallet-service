package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.entity.AuditEntity;

/**
 * Maps between domain entities and data access entities.
 */
public class AuditDataAccessMapper {

    public AuditItem auditEntityToAuditItem(AuditEntity entity) {
        return AuditItem.builder()
                .id(entity.getId())
                .info(entity.getInfo())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AuditEntity auditItemToAuditEntity(AuditItem auditItem) {
        return AuditEntity.builder()
                .id(auditItem.getId())
                .info(auditItem.getInfo())
                .createdAt(auditItem.getCreatedAt())
                .build();
    }
}
