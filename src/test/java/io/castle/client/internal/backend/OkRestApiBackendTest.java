package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
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
        ExampleProperties example = new ExampleProperties();
        example.setExample("authentication");
        JsonElement jsonElement = castleSdkInternalConfiguration.getModel().getGson().toJsonTree(example);

        //When
        AuthenticateAction authenticateAction = restApi.sendAuthenticateSync("$login.succeeded", "1234", null, jsonElement);

        //Then response code is correct
        assertEquals(AuthenticateAction.ALLOW, authenticateAction);
    }

    @Test
    public void testTrackRequest() {
        //Given
        RestApi restApi = castleSdkInternalConfiguration.getRestApiFactory().buildBackend();
        ExampleProperties example = new ExampleProperties();
        example.setExample("track");
        JsonElement jsonElement = castleSdkInternalConfiguration.getModel().getGson().toJsonTree(example);

        //When
        int sendTrackStatus = restApi.sendTrackRequest("$login.succeeded", "1234", null, jsonElement);

        //Then response code is correct
        assertEquals(201, sendTrackStatus);
    }

    private static class ExampleProperties {
        private String example;

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }
    }
}