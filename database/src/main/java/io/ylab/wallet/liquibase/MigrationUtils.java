package io.ylab.wallet.liquibase;

import io.ylab.wallet.connection.ConnectionManager;
import io.ylab.wallet.domain.config.PropertiesUtils;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;

import java.sql.Connection;

/**
 * Utility class that manages liquibase configuration and migrations.
 */
@UtilityClass
public class MigrationUtils {

    public final String LIQUIBASE_SCHEMA_KEY = "liquibase.schema";
    public final String LIQUIBASE_CHANGELOG_KEY = "liquibase.changelog";
    public final String LIQUIBASE_TEST_CHANGELOG_KEY = "liquibase.testChangelog";

    public String liquibaseSchemaName = PropertiesUtils.get(LIQUIBASE_SCHEMA_KEY);
    public String changelog = PropertiesUtils.get(LIQUIBASE_CHANGELOG_KEY);

    /**
     * Runs liquibase migrations.
     */
    public void runMigrations() {
        try (Connection connection = ConnectionManager.open()) {
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

    public void setTestChangelog() {
        MigrationUtils.changelog = PropertiesUtils.get(LIQUIBASE_TEST_CHANGELOG_KEY);
    }

    public void setDefaultSchema() {
        liquibaseSchemaName = "public";
    }
}
