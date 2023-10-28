package io.ylab.wallet.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Utility class for opening jdbc connection.
 */
@Component
public class ConnectionManager {

    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

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
    public Connection open() {
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
    public void setConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
