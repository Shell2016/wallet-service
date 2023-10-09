package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service that contains audit business logic.
 */
@RequiredArgsConstructor
public class AuditService {

    /**
     * Injection of repository to persist audit info.
     */
    private final AuditRepository auditRepository;

    public void audit(String info) {
        auditRepository.addAuditItem(AuditItem.builder()
                .time(LocalDateTime.now())
                .info(info)
                .build());
    }

    /**
     * Gets list of audits.
     * @return list of audits
     */
    public List<AuditItem> getAuditInfo() {
        return auditRepository.getAuditInfo();
    }
}
