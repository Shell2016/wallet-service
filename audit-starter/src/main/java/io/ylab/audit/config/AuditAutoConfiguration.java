package io.ylab.audit.config;

import io.ylab.audit.aop.AuditAspect;
import io.ylab.wallet.domain.service.AuditService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * This configuration enabled by default.
 * Audit can be disabled with property: audit.enabled=false
 */
@Slf4j
@AutoConfiguration
@ConditionalOnBean(AuditService.class)
@ConditionalOnProperty(prefix = "audit", value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AuditProperties.class)
public class AuditAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("AuditAutoConfiguration initialized");
    }

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}
