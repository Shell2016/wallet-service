package io.ylab.audit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "audit")
public record AuditProperties(@DefaultValue("true") boolean enabled) {
}
