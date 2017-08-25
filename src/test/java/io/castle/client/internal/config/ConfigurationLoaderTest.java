package io.castle.client.internal.config;

import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.Properties;

public class ConfigurationLoaderTest {

    @Test
    public void loadConfigWithNullWhitelist() throws CastleSdkConfigurationException {
        //given
        Properties properties = new Properties();
        properties.setProperty("api_secret", "212312");
        properties.setProperty("app_id", "F");
        properties.setProperty("black_list", "TestBlack,Cookie");
        ConfigurationLoader loader = new ConfigurationLoader(properties);
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("212312")
                .withCastleAppId("F")
                .withBlackListHeaders("TestBlack", "Cookie")
                .withDefaultWhitelist()
                .withDefaultBackendProvider()
                .build();

        //when
        CastleConfiguration castleConfiguration = loader.loadConfiguration();

        //Then the configuration should use the default whitelist headers
        Assertions.assertThat(castleConfiguration).isEqualToComparingFieldByFieldRecursively(expectedConfiguration);
    }

    @Test
    public void loadConfigWithNullBlacklist() throws CastleSdkConfigurationException {
        //given
        Properties properties = new Properties();
        properties.setProperty("api_secret", "212312");
        properties.setProperty("app_id", "F");
        properties.setProperty("white_list", "TestWhite");
        ConfigurationLoader loader = new ConfigurationLoader(properties);
        CastleConfiguration expectedConfiguration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("212312")
                .withCastleAppId("F")
                .withWhiteListHeaders("TestWhite")
                .withDefaultBlacklist()
                .withDefaultBackendProvider()
                .build();

        //when
        CastleConfiguration castleConfiguration = loader.loadConfiguration();

        //Then the configuration should use the default blacklist headers
        Assertions.assertThat(castleConfiguration).isEqualToComparingFieldByFieldRecursively(expectedConfiguration);
    }

    @Rule
    public final EnvironmentVariables environmentVariablesAPISecret
            = new EnvironmentVariables();

    @Test
    public void loadApiSecretFromEnv() throws CastleSdkConfigurationException {

        //given
        environmentVariables.set("CASTLE_SDK_API_SECRET", "1234");
        ConfigurationLoader loader = new ConfigurationLoader();

        //when
        CastleConfiguration castleConfiguration = loader.loadConfiguration();


        //Then the value of the API secret in the environmental variable should be chosen over the value in the properties file
        Assert.assertEquals("1234", System.getenv("CASTLE_SDK_API_SECRET"));
        Assertions.assertThat(castleConfiguration.getApiSecret()).isEqualTo("1234");
    }

    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();

    @Test(expected = CastleSdkConfigurationException.class)
    public void tryToLoadConfigurationsFromAWrongFile() throws CastleSdkConfigurationException {
        //given
        environmentVariables.set("CASTLE_PROPERTIES_FILE", "castle_sdk.wrongproperties");
        ConfigurationLoader loader = new ConfigurationLoader();

        Assert.assertEquals("castle_sdk.wrongproperties", System.getenv("CASTLE_PROPERTIES_FILE"));
        //when
        CastleConfiguration castleConfiguration = loader.loadConfiguration();

        //TODO: generate file input stream error.

    }


}
