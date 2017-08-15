package io.castle.client.internal.backend;

import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.model.AuthenticateAction;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

//Temp test to fast check of http layer. Setup env variable CASTLE_SDK_API_SECRET to run the test
@Ignore
public class OkRestApiBackendTest {

    CastleSdkInternalConfiguration castleSdkInternalConfiguration = CastleSdkInternalConfiguration.getInternalConfiguration();

    @Test
    public void testAuthenticationRequest() {
        //Given
        RestApi restApi = castleSdkInternalConfiguration.getRestApiFactory().buildBackend();
        //When
        AuthenticateAction authenticateAction = restApi.sendAuthenticateSync("$login.succeeded", "1234", "{}", null);

        //Then response code is correct
        assertEquals(AuthenticateAction.ALLOW, authenticateAction);
    }

    @Test
    public void testTrackRequest() {
        //Given
        RestApi restApi = castleSdkInternalConfiguration.getRestApiFactory().buildBackend();
        //When
        int sendTrackStatus = restApi.sendTrackRequest("$login.succeeded", "1234", "{}", null);

        //Then response code is correct
        assertEquals(201, sendTrackStatus);
    }

}