package io.ylab.logging.config;

import io.ylab.logging.aop.LoggingExecutionTimeAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for logging execution time of controller methods.
 * To enable this configuration use annotation @EnableExecutionTimeLogging over any @Configuration class
 * or just @Import(ExecutionTimeLoggingConfiguration.class)
 */
@Slf4j
@Configuration
public class ExecutionTimeLoggingConfiguration {

    @PostConstruct
    void init() {
        log.info("ExecutionTimeLoggingConfiguration initialized");
    }

    @Bean
    public LoggingExecutionTimeAspect loggingExecutionTimeAspect() {
        return new LoggingExecutionTimeAspect();
    }
}
