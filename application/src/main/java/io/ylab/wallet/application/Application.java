package io.ylab.wallet.application;

import io.ylab.wallet.liquibase.MigrationUtils;

/**
 * Main class that starts application.
 */
public class Application {
    public static void main(String[] args) {
        runMigrations();
    }

    /**
     * runs liquibase migrations
     */
    private static void runMigrations() {
        MigrationUtils.runMigrations();
    }
}
