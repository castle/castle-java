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

    @Test
    public void sdkOnTestLoadTheCorrectConfiguration() throws CastleSdkConfigurationException {
        //Given
        Castle.verifySdkConfigurationAndInitialize();

        //When the sdk is initiated
        Castle sdk = Castle.sdk();

        //Then the configuration match the expected values from the class path properties
        CastleConfiguration sdkConfiguration = sdk.getSdkConfiguration();
        Assertions.assertThat(sdkConfiguration)
                .extracting("apiSecret", "castleAppId")
                .containsExactly("test_api_secret", "test_app_id");

        Assertions.assertThat(sdkConfiguration.getBlackListHeaders())
                .containsExactlyInAnyOrder(
                        "cookie",
                        "testblack"
                );
        Assertions.assertThat(sdkConfiguration.getWhiteListHeaders())
                .containsExactlyInAnyOrder(
                        "testwhite",
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
        Castle.verifySdkConfigurationAndInitialize();

        //When the sdk is loaded two times
        Castle sdk1 = Castle.sdk();
        Castle sdk2 = Castle.sdk();

        //Then the same singleton instance is returned
        Assertions.assertThat(sdk1).isSameAs(sdk2);
    }


    @Test(expected = IllegalStateException.class)
    public void sdkWithoutInitializationThrowAIllegalStateException() {

        //Given

        //When the sdk is called without initialization
        Castle.sdk();

        //Then exception is throw
    }


}