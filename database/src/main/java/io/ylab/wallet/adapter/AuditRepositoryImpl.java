package io.ylab.wallet.adapter;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import io.ylab.wallet.mapper.AuditDataAccessMapper;
import io.ylab.wallet.repository.JdbcAuditRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Adapter between AuditRepository and JdbcAuditRepository.
 */
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    /**
     * Concrete repository that responsible for processing audit data.
     */
    private final JdbcAuditRepository jdbcAuditRepository;
    /**
     * Maps between domain entities and data access entities.
     */
    private final AuditDataAccessMapper auditMapper;

    @Override
    public void save(AuditItem auditItem) {
        jdbcAuditRepository.save(auditMapper.auditItemToAuditEntity(auditItem));
    }

    @Override
    public List<AuditItem> getAuditItems() {
        return jdbcAuditRepository.getAuditItems().stream()
                .map(auditMapper::auditEntityToAuditItem)
                .toList();
    }
}
