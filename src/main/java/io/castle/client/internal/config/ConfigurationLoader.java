package io.castle.client.internal.config;

import com.google.common.base.Splitter;
import io.castle.client.Castle;
import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Loads the SDK configuration from the environment or classpath, granting precedence to the environment.
 * <p>
 * The following criteria are used when loading a configuration value:
 * <ol>
 * <li> environment values take precedence over values written in properties files;
 * <li> if no environment value is provided, then a value from classpath properties file is taken;
 * <li> If neither an environment variable nor a value in a properties file is provided, default values are set for some
 * fields in the resulting {@link CastleConfiguration}.
 * </ol><p>
 * It is necessary to at least provide values for the API Secret, since it does not have a
 * default value.
 * <p>
 * Properties are loaded from file {@code castle_sdk.properties} by default.
 * In order to specify a different file, set the {@code CASTLE_PROPERTIES_FILE} environmental variable.
 */
class ConfigurationLoader {

    /**
     * An instance of Properties that can be used instead of environmental variables or a properties file.
     */
    private final Properties castleConfigurationProperties;

    /**
     * Creates a configurationLoader that will load a castleConfiguration from an instance of Properties.
     *
     * @param properties properties containing the values that will be passed to the castleConfiguration loaded.
     */
    ConfigurationLoader(Properties properties) {
        this.castleConfigurationProperties = properties;
    }

    /**
     * Creates a configurationLoader that will load a castleConfiguration from a properties file.
     * <p>
     * A properties file in the classpath will be loaded if the {@code CASTLE_PROPERTIES_FILE} is set to its name.
     * If this variable is not set, then the file named {@code castle_sdk.properties} in the classpath will be loaded,
     * provided that it exists.
     */
    ConfigurationLoader() {
        this(loadPropertiesFile());
    }

    private static Properties loadPropertiesFile() {
        String propertyFile = getPropertiesFilePath();
        Properties loaded = new Properties();
        URL configFile = Castle.class.getClassLoader().getResource(propertyFile);
        if (configFile != null) {
            InputStream resourceAsStream = Castle.class.getClassLoader().getResourceAsStream(propertyFile);
            loaded = new PropertiesReader().loadPropertiesFromStream(loaded, resourceAsStream);
        }
        return loaded;
    }

    private static String getPropertiesFilePath() {
        String propertiesFile = System.getenv("CASTLE_PROPERTIES_FILE");
        if (propertiesFile != null) {
            return propertiesFile;
        } else {
            return "castle_sdk.properties";
        }
    }

    /**
     * Loads the application level configuration for the Castle SDK with values taken from environmental variables,
     * a properties file or default values in that order of precedence.
     * <p>
     * When wrong failover strategy or backend, null is returned instead.
     *
     * @return a CastleConfiguration instance
     * @throws CastleSdkConfigurationException if at least one of apiSecret or castleAppId is not provided in either the
     *                                         environment or the properties file in the classpath
     * @throws NumberFormatException           if the provided timeout environmental variable or property is not
     *                                         parsable into an int
     */
    public CastleConfiguration loadConfiguration() throws CastleSdkConfigurationException, NumberFormatException {
        CastleConfigurationBuilder builder = loadConfigurationBuilder();
        return builder.build();
    }

    public CastleConfigurationBuilder loadConfigurationBuilder() {
        String envApiSecret = loadConfigurationValue(
                castleConfigurationProperties,
                "api_secret",
                "CASTLE_SDK_API_SECRET"
        );
        String castleAppId = loadConfigurationValue(
                castleConfigurationProperties,
                "app_id",
                "CASTLE_SDK_APP_ID"
        );
        String whiteListValue = loadConfigurationValue(
                castleConfigurationProperties,
                "white_list",
                "CASTLE_SDK_WHITELIST_HEADERS"
        );
        String blackListValue = loadConfigurationValue(
                castleConfigurationProperties,
                "black_list",
                "CASTLE_SDK_BLACKLIST_HEADERS"
        );
        String timeoutValue = loadConfigurationValue(
                castleConfigurationProperties,
                "timeout",
                "CASTLE_SDK_TIMEOUT"
        );
        String backendProviderValue = loadConfigurationValue(
                castleConfigurationProperties,
                "backend_provider",
                "CASTLE_SDK_BACKEND_PROVIDER"
        );
        String authenticateFailoverStrategyValue = loadConfigurationValue(
                castleConfigurationProperties,
                "failover_strategy",
                "CASTLE_SDK_AUTHENTICATE_FAILOVER_STRATEGY"
        );
        String apiBaseUrl = loadConfigurationValue(
                castleConfigurationProperties,
                "base_url",
                "CASTLE_SDK_BASE_URL"
        );
        String logHttpRequests = loadConfigurationValue(
                castleConfigurationProperties,
                "log_http",
                "CASTLE_SDK_LOG_HTTP"
        );
        CastleConfigurationBuilder builder = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret(envApiSecret)
                .withCastleAppId(castleAppId);
        if (apiBaseUrl != null) {
            builder.withApiBaseUrl(apiBaseUrl);
        } else {
            builder.withDefaultApiBaseUrl();
        }
        if (whiteListValue != null) {
            builder.withWhiteListHeaders(Splitter.on(",").splitToList(whiteListValue));
        }
        if (blackListValue != null) {
            builder.withBlackListHeaders(Splitter.on(",").splitToList(blackListValue));
        }
        if (timeoutValue != null) {
            // might throw NumberFormatException if string is not parsable to int
            int timeout = Integer.parseInt(timeoutValue);
            builder.withTimeout(timeout);
        }
        if (authenticateFailoverStrategyValue != null) {
            if (authenticateFailoverStrategyValue.compareTo("throw") == 0) {
                builder.withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy());
            } else {
                builder.withAuthenticateFailoverStrategy(
                        new AuthenticateFailoverStrategy(
                                AuthenticateAction.fromAction(authenticateFailoverStrategyValue)
                        )
                );
            }
        } else {
            builder.withDefaultAuthenticateFailoverStrategy();
        }
        if (backendProviderValue != null) {
            builder.withBackendProvider(
                    CastleBackendProvider.valueOf(backendProviderValue)
            );
        }
        if (logHttpRequests != null) {
            builder.withLogHttpRequests(Boolean.valueOf(logHttpRequests));
        }

        return builder;
    }

    private String loadConfigurationValue(Properties properties, String propertyName, String environmentName) {
        String getenv = System.getenv(environmentName);
        if (getenv == null) {
            getenv = properties.getProperty(propertyName);
        }
        return getenv;
    }

}
