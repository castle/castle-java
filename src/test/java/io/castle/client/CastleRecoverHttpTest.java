package io.castle.client;

import io.castle.client.model.*;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleRecoverHttpTest extends AbstractCastleHttpLayerTest {

    public CastleRecoverHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void recoverDevices() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(DeviceUtils.getDefaultDeviceJSON());
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an report device request is made
        CastleResponse response = sdk.onRequest(request).recover("1234");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/users/1234/recover"), recordedRequest.getRequestUrl());
        Assert.assertEquals("PUT", recordedRequest.getMethod());
    }
}
