package io.ylab.wallet.domain.config;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        replacePlaceholders();
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

    /**
     * Replaces placeholders from application.properties.
     */
    private static void replacePlaceholders() {
        PROPERTIES.forEach((key, value) -> {
            if (value instanceof String) {
                String resolvedValue = resolvePlaceholders((String) value);
                PROPERTIES.setProperty((String) key, resolvedValue);
            }
        });
    }

    /**
     * Resolves and replaces placeholders to environment variables if they present in the system.
     * @param value to resolve and replace if necessary
     * @return final value that will be set in properties set
     */
    private static String resolvePlaceholders(String value) {
        String regex = "\\$\\{([^:]+)(?::([^}]+))?\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String defaultValue = matcher.group(2);
            String envValue = System.getenv(placeholder);
            String finalDefaultValue = defaultValue != null ? defaultValue : "";
            String replacement = envValue != null ? envValue : finalDefaultValue;
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
