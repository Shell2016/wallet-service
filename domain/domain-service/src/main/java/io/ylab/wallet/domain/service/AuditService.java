package io.ylab.wallet.domain.service;

import io.ylab.wallet.domain.port.output.repository.AuditRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;
}
