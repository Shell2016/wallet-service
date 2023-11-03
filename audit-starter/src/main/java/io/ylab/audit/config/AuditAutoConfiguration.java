package io.ylab.audit.config;

import io.ylab.audit.aop.AuditAspect;
import io.ylab.audit.repository.AuditRepository;
import io.ylab.audit.repository.JdbcAuditRepository;
import io.ylab.audit.service.AuditService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * This configuration enabled by default.
 * Audit can be disabled with property: audit.enabled=false
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(AuditProperties.class)
@ConditionalOnProperty(prefix = "audit", value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AuditProperties.class)
public class AuditAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("AuditAutoConfiguration initialized");
    }

    @Bean
    public AuditRepository auditRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcAuditRepository(jdbcTemplate);
    }

    @Bean
    public AuditService auditService(AuditRepository auditRepository) {
        return new AuditService(auditRepository);
    }

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}
