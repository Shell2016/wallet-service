package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.dto.AuditItem;
import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    public void audit(String info) {
        auditRepository.addAuditItem(AuditItem.builder()
                .time(LocalDateTime.now())
                .info(info)
                .build());
    }

    public List<AuditItem> getAuditInfo() {
        return auditRepository.getAuditInfo();
    }
}
