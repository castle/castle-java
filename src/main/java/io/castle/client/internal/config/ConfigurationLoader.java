package io.castle.client.internal.config;

import com.google.common.base.Splitter;
import io.castle.client.Castle;
import io.castle.client.model.CastleSdkConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Load the SDK configuration using the following criteria:
 * 1) Environment values have precedence.
 * 2) If no environment value is provided, then try value from classpath property file.
 * <p>
 * Properties are loaded from file castle_sdk.properties.
 */
class ConfigurationLoader {

    static String SDK_PROPERTY_FILENAME;

    private final Properties castleConfigurationProperties;

    ConfigurationLoader(Properties properties) {
        this.castleConfigurationProperties = properties;
    }

    ConfigurationLoader() {
        this(loadPropertiesFile());
    }

    private static Properties loadPropertiesFile() {
        SDK_PROPERTY_FILENAME = setPropertiesFilePath();
        Properties loaded = new Properties();
        URL configFile = Castle.class.getClassLoader().getResource(ConfigurationLoader.SDK_PROPERTY_FILENAME);
        if (configFile != null) {
            try (final InputStream streamFromFile = Castle.class.getClassLoader().getResourceAsStream(ConfigurationLoader.SDK_PROPERTY_FILENAME)) {
                loaded.load(streamFromFile);
            } catch (IOException e) {
                //OK, no file configuration, create a empty new properties just for security against side effects of the failed load operation
                loaded = new Properties();
            }
        }
        return loaded;
    }


    public CastleConfiguration loadConfiguration() throws CastleSdkConfigurationException {
        String envApiSecret = loadConfigurationValue(castleConfigurationProperties, "api_secret", "CASTLE_SDK_API_SECRET");
        String castleAppId = loadConfigurationValue(castleConfigurationProperties, "app_id", "CASTLE_SDK_APP_ID");
        String whiteListValue = loadConfigurationValue(castleConfigurationProperties, "white_list", "CASTLE_SDK_WHITELIST_HEADERS");
        String blackListValue = loadConfigurationValue(castleConfigurationProperties, "black_list", "CASTLE_SDK_BLACKLIST_HEADERS");
        CastleConfigurationBuilder builder = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret(envApiSecret)
                .withCastleAppId(castleAppId);
        if (whiteListValue != null) {
            builder.withWhiteListHeaders(Splitter.on(",").splitToList(whiteListValue));
        }
        if (blackListValue != null) {
            builder.withBlackListHeaders(Splitter.on(",").splitToList(blackListValue));
        }
        return builder.build();
    }

    private String loadConfigurationValue(Properties properties, String propertyName, String environmentName) {
        String envApiSecret = System.getenv(environmentName);
        if (envApiSecret == null) {
            envApiSecret = properties.getProperty(propertyName);
        }
        return envApiSecret;
    }

    private static String setPropertiesFilePath(){
        String propertiesFile = System.getenv("CASTLE_PROPERTIES_FILE");
        if (propertiesFile != null){
            return propertiesFile;
        } else {
            return "castle_sdk.properties";
        }
    }
}
