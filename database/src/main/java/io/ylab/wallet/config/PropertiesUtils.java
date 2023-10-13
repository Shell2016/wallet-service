package io.ylab.wallet.config;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class that parses 'application.properties' file and stores application properties.
 */
@UtilityClass
public class PropertiesUtils {

    /**
     * Persistent set of properties.
     */
    private final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Gets property for key.
     * @param key of the property
     * @return property
     */
    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Loads properties from 'application.properties' file to Properties persistent set.
     */
    private static void loadProperties() {
        try (var inputStream = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
