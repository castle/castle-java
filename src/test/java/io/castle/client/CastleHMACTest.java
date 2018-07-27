package io.castle.client;

import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleHMACTest {

    @Test
    public void calculateHMACSecureValue() throws CastleSdkConfigurationException {

        //Given
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        String userId = "TestUserID";

        //When the sdk is loaded two times
        String secureUserID = sdk.secureUserID(userId);
        // then a correct HMAC is calculated
        Assertions.assertThat(secureUserID).isEqualTo("eeef03d7fb117ce8f30b8fc49d3cd23c3585abb520e133b2c1ac273f8b86fa75");
    }
}
