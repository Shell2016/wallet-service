package io.ylab.wallet.liquibase;

import io.ylab.wallet.connection.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * Utility class that manages liquibase configuration and migrations.
 */
@Component
@RequiredArgsConstructor
public class MigrationRunner {

    private final ConnectionManager connectionManager;
    @Value("${liquibase.schema}")
    private String liquibaseSchemaName;
    @Value("${liquibase.changelog}")
    private String changelog;
    @Value("${liquibase.testChangelog}")
    private String testChangelog;

    /**
     * Runs liquibase migrations.
     */
    public void runMigrations() {
        try (Connection connection = connectionManager.open()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(liquibaseSchemaName);
            Liquibase liquibase = new Liquibase(
                    changelog,
                    new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs liquibase migrations for testcontainers.
     */
    public void runTestMigrations() {
        try (Connection connection = connectionManager.open()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("public");
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-test.xml",
                    new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
