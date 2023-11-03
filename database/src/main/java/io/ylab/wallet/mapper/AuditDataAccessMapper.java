package io.ylab.wallet.mapper;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.entity.AuditEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain entities and data access entities.
 */
@Mapper(componentModel = "spring")
public interface AuditDataAccessMapper {
    AuditItem auditEntityToAuditItem(AuditEntity entity);
    AuditEntity auditItemToAuditEntity(AuditItem auditItem);
}
