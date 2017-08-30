package io.castle.client.internal.config;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.Properties;

public class ConfigurationLoaderTest {

    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();

    @Test
    public void loadConfigWithNullValuesExceptForAppIDAndPISecret() throws CastleSdkConfigurationException {
        Properties properties = new Properties();
        properties.setProperty("api_secret", "212312");
        properties.setProperty("app_id", "F");
        properties.setProperty("base_url", "http://valid.value.com");
        ConfigurationLoader loader = new ConfigurationLoader(properties);
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("212312")
                .withCastleAppId("F")
                .withApiBaseUrl("http://valid.value.com")
                .build();

        //when
        CastleConfiguration castleConfiguration = loader.loadConfiguration();

        //Then the configuration should use the defaults
        Assertions.assertThat(castleConfiguration).isEqualToComparingFieldByFieldRecursively(expectedConfiguration);

    }

    @Test
    public void loadFromProperties() throws CastleSdkConfigurationException {
        //given
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .aConfigBuilder()
                .withApiSecret("test_api_secret")
                .withCastleAppId("test_app_id")
                .withApiBaseUrl("https://testing.api.dev.castle/v1/")
                .withWhiteListHeaders(
                        "TestWhite",
                        "User-Agent",
                        "Accept-Language",
                        "Accept-Encoding",
                        "Accept-Charset",
                        "Accept",
                        "Accept-Datetime",
                        "X-Forwarded-For",
                        "Forwarded",
                        "X-Forwarded",
                        "X-Real-IP",
                        "REMOTE_ADDR"
                )
                .withBlackListHeaders(
                        "TestBlack",
                        "Cookie"
                )
                .withDefaultBackendProvider()
                .withTimeout(100)
                .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE))
                .build();

        //Then the value of the timeout should be the one in the properties file
        testLoad(expectedConfiguration);
    }

    @Test
    public void loadFromEnv() throws CastleSdkConfigurationException {
        setEnvAndTestCorrectness(
                "CASTLE_SDK_API_SECRET",
                "1234"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_TIMEOUT",
                "700"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_AUTHENTICATE_FAILOVER_STRATEGY",
                "DENY"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_WHITELIST_HEADERS",
                "Accept-Encoding,Accept-Charset"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_BLACKLIST_HEADERS",
                "TestBlackEnv,Cookie"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_APP_ID",
                "test_app_id_env"
        );

        setEnvAndTestCorrectness(
                "CASTLE_SDK_BASE_URL",
                "https://api.dev.castle.io/v1/"
        );

        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .aConfigBuilder()
                .withApiSecret("1234")
                .withCastleAppId("test_app_id_env")
                .withWhiteListHeaders(
                        "Accept-Encoding",
                        "Accept-Charset"
                )
                .withBlackListHeaders(
                        "TestBlackEnv",
                        "Cookie"
                )
                .withApiBaseUrl("https://api.dev.castle.io/v1/")
                .withTimeout(700)
                .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.DENY))
                .build();

        testLoad(expectedConfiguration);
    }

    @Test
    public void loadFailoverThrowStrategyFromEnv() throws CastleSdkConfigurationException {
        //given a property file not existing is provided
        setEnvAndTestCorrectness("CASTLE_PROPERTIES_FILE", "notExistingFile.properties");
        // and a minimal sdk configuration is provided in environment
        setEnvAndTestCorrectness(
                "CASTLE_SDK_APP_ID",
                "test_app_id_env"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_API_SECRET",
                "1234"
        );
        // and the failover strategy environment value is throw
        setEnvAndTestCorrectness(
                "CASTLE_SDK_AUTHENTICATE_FAILOVER_STRATEGY",
                "throw"
        );
        // and a expected config is the default configuration with the throw strategy
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("1234")
                .withCastleAppId("test_app_id_env")
                .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy())
                .build();

        // when then
        testLoad(expectedConfiguration);
    }


    @Test
    public void ignoreNonExistingPropertiesFileTest() throws CastleSdkConfigurationException {
        //given a property file not existing is provided
        setEnvAndTestCorrectness("CASTLE_PROPERTIES_FILE", "notExistingFile.properties");
        ConfigurationLoader loader = new ConfigurationLoader();
        //and a minimal setup on env is provided
        setEnvAndTestCorrectness(
                "CASTLE_SDK_API_SECRET",
                "1234"
        );
        setEnvAndTestCorrectness(
                "CASTLE_SDK_APP_ID",
                "test_app_id_env"
        );
        //Then configuration is loaded correctly and the default values are used
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder.defaultConfigBuilder()
                .withApiSecret("1234")
                .withCastleAppId("test_app_id_env")
                .build();
        testLoad(expectedConfiguration);

    }

    @Test(expected = NumberFormatException.class)
    public void testTimeoutWithNonParsableInt() throws CastleSdkConfigurationException {
        //given
        Properties properties = new Properties();
        properties.setProperty("api_secret", "212312");
        properties.setProperty("app_id", "F");
        properties.setProperty("timeout", "UnparsableInt");
        ConfigurationLoader loader = new ConfigurationLoader(properties);

        //then
        loader.loadConfiguration();

        //then an exception is throw, since the provided timeout cannot be parsed into an int.
    }

    @Test(expected = CastleSdkConfigurationException.class)
    public void tryToLoadConfigurationsFromAWrongFile() throws CastleSdkConfigurationException {
        //given
        setEnvAndTestCorrectness("CASTLE_PROPERTIES_FILE", "castle_sdk.wrongproperties");
        ConfigurationLoader loader = new ConfigurationLoader();
        //when
        loader.loadConfiguration();
    }

    private void setEnvAndTestCorrectness(String variable, String value) throws CastleSdkConfigurationException {
        //given
        environmentVariables.set(variable, value);
        // then the value for the configuration should be the one set in the environmental variable.
        Assert.assertEquals(value, System.getenv(variable));
    }

    private void testLoad(CastleConfiguration expected) throws CastleSdkConfigurationException {
        ConfigurationLoader loader = new ConfigurationLoader();
        //then the loaded configuration should equal field to field with the expected.
        Assertions.assertThat(loader.loadConfiguration())
                .isEqualToComparingFieldByFieldRecursively(expected);
    }

}
