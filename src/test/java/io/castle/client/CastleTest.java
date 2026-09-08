package io.castle.client;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleTest {

    @Test
    public void sdkVerificationPassBecauseOfClassPathConfiguration() throws CastleSdkConfigurationException {
        //Given

        //When
        Castle.verifySdkConfigurationAndInitialize();
        //Then
        // No exception is throw, because the tests classpath contains the file castle_sdk.properties
    }

    /**
     *
     * Properties are loaded from the default configuration file in test/resources/castle_sdk.properties
     *
     * @throws CastleSdkConfigurationException
     */
    @Test
    public void sdkOnTestLoadTheCorrectConfiguration() throws CastleSdkConfigurationException {
        //When the sdk is initiated
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();

        //Then the configuration match the expected values from the class path properties
        CastleConfiguration sdkConfiguration = sdk.getSdkConfiguration();
        Assertions.assertThat(sdkConfiguration)
                .extracting("apiSecret", "castleAppId")
                .containsExactly("test_api_secret", "test_app_id");

        Assertions.assertThat(sdkConfiguration.getDenyListHeaders())
                .containsExactlyInAnyOrder(
                        "cookie",
                        "testdeny"
                );
        Assertions.assertThat(sdkConfiguration.getAllowListHeaders())
                .containsExactlyInAnyOrder(
                        "testallow",
                        "user-agent",
                        "accept-language",
                        "accept-encoding",
                        "accept-charset",
                        "accept",
                        "accept-datetime",
                        "x-forwarded-for",
                        "forwarded",
                        "x-forwarded",
                        "x-real-ip",
                        "remote-addr"
                );
    }

    @Test(expected = IllegalStateException.class)
    public void sdkWithoutInitializationThrowAIllegalStateException() {

        //Given

        //When the sdk is called without initialization
        Castle.instance();

        //Then exception is thrown
    }

    @Test
    public void sdkProvideASingleton() throws CastleSdkConfigurationException {

        //Given
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        Castle.setSingletonInstance(sdk);

        //When the sdk is loaded two times
        Castle sdk1 = Castle.instance();
        Castle sdk2 = Castle.instance();

        //Then the same singleton instance is returned
        Assertions.assertThat(sdk1).isSameAs(sdk2);
        sdk.close();
    }

    @Test
    public void sdkOnConfigureLoadsDefault() throws CastleSdkConfigurationException {

        //When the sdk is initiated
        Castle sdk = Castle.initialize();

        //Then the configuration match the expected values from the class path properties
        CastleConfiguration sdkConfiguration = sdk.getSdkConfiguration();
        Assertions.assertThat(sdkConfiguration)
                .extracting("apiSecret", "castleAppId")
                .containsExactly("test_api_secret", "test_app_id");
    }

    @Test
    public void sdkOnConfigureWithBuilder() throws CastleSdkConfigurationException {

        // Initialize sdk
        Castle sdk = Castle.initialize(
            Castle.configurationBuilder()
                .apiSecret("abcd")
                .appId("1234")
                .build()
        );

        //Then the configuration match the expected values from the class path properties
        CastleConfiguration sdkConfiguration = sdk.getSdkConfiguration();
        Assertions.assertThat(sdkConfiguration)
                .extracting("apiSecret", "castleAppId")
                .containsExactly("abcd", "1234");
    }

    @Test
    public void sdkOnConfigureWithSecretOnly() throws CastleSdkConfigurationException {
        // When the SDK is initiated
        Castle sdk = Castle.initialize("abcd");

        //Then the configuration match the expected values from the initialization
        CastleConfiguration sdkConfiguration = sdk.getSdkConfiguration();
        Assertions.assertThat(sdkConfiguration)
                .extracting("apiSecret")
                .isEqualTo("abcd");
        sdk.close();
    }

    @Test
    public void closeReleasesHttpClient() throws CastleSdkConfigurationException {
        Castle sdk = Castle.initialize("abcd");
        sdk.close();
    }

    @Test
    public void closeClearsTheSingletonWhenThisInstanceIsTheSingleton() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        Castle.setSingletonInstance(sdk);

        sdk.close();

        Assertions.assertThatThrownBy(Castle::instance)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void closeDoesNotClearADifferentSingleton() throws CastleSdkConfigurationException {
        Castle singleton = Castle.verifySdkConfigurationAndInitialize();
        Castle.setSingletonInstance(singleton);
        Castle other = Castle.initialize("abcd");

        other.close();

        Assertions.assertThat(Castle.instance()).isSameAs(singleton);
        singleton.close();
    }
}
