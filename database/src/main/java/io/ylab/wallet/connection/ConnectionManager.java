package io.ylab.wallet.connection;

import io.ylab.wallet.domain.config.PropertiesUtils;
import lombok.experimental.UtilityClass;

import java.sql.*;

/**
 * Utility class for opening jdbc connection.
 */
@UtilityClass
public final class ConnectionManager {

    private final String URL_KEY = "db.url";
    private final String USERNAME_KEY = "db.username";
    private final String PASSWORD_KEY = "db.password";

    private String url = PropertiesUtils.get(URL_KEY);
    private String username = PropertiesUtils.get(USERNAME_KEY);
    private String password = PropertiesUtils.get(PASSWORD_KEY);

    static {
        try {
            loadDriver();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    /**
     * Opens connection with required parameters.
     *
     * @return jdbc Connection
     */
    public static Connection open() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Overrides default config from application.properties file.
     * Can be used for example for configuring connection for testcontainers.
     *
     * @param url      database url
     * @param username database username
     * @param password database password
     */
    public static void setConfig(String url, String username, String password) {
        ConnectionManager.url = url;
        ConnectionManager.username = username;
        ConnectionManager.password = password;
    }
}
