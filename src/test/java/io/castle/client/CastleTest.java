package io.castle.client;

import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

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
    }


    @Test
    public void sdkInstanceCanBeModifiedForTestProposes() throws CastleSdkConfigurationException, NoSuchFieldException, IllegalAccessException {
        //Given a SDK instance
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        RestApiFactory mockFactory = Mockito.mock(RestApiFactory.class);
        //When the utils are used to override the internal backend factory
        SdkMockUtil.modifyInternalBackendFactory(sdk, mockFactory);
        //Then the internal rest factory is mocked
        Assertions.assertThat(sdk.getInternalConfiguration().getRestApiFactory()).isSameAs(mockFactory);
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
                .containsExactly("abcd");
    }
}
