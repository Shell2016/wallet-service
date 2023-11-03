package io.ylab.wallet.repository;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.*;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Configures context and testcontainers for jdbc tests.
 * Starts one container for all test classes for test speed up.
 */
@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class JdbcIntegrationTestBase {

    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine");

    /**
     * Starts once for all test classes that extends this base class.
     */
    @BeforeAll
    static void runContainer() {
        CONTAINER.start();
    }

    /**
     * Dynamically sets properties from started testcontainer into Environment and then to datasource.
     */
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
    }
}
