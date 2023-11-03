package io.ylab.wallet;

import io.ylab.logging.config.EnableExecutionTimeLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts application context and runs application.
 */
@SpringBootApplication
@EnableExecutionTimeLogging
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }
}
