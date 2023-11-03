package io.ylab.logging.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * To enable starter use this annotation over any @Configuration class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ExecutionTimeLoggingConfiguration.class)
public @interface EnableExecutionTimeLogging {
}


