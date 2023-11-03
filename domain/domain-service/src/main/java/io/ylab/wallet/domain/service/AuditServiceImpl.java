package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service that contains audit business logic.
 */
@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    /**
     * Injection of repository to persist audit info.
     */
    private final AuditRepository auditRepository;

    /**
     * This method invoked in Advices and shouldn't depend on outgoing transactions.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void audit(String info) {
        auditRepository.save(AuditItem.builder()
                .createdAt(LocalDateTime.now())
                .info(info)
                .build());
    }
}
