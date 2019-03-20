package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleRuntimeException;
import io.castle.client.model.CastleUserDevice;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleDeviceReportHttpTest extends AbstractCastleHttpLayerTest {

    public CastleDeviceReportHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void reportDevice() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(DeviceUtils.getDefaultDeviceJSON());
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an report device request is made
        CastleUserDevice device = sdk.onRequest(request).report("deviceToken");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/devices/deviceToken/report"), recordedRequest.getRequestUrl());
        Assert.assertEquals("PUT", recordedRequest.getMethod());

        // and the correct Review model object is extracted
        CastleUserDevice expected = DeviceUtils.createExpectedDevice();

        Assertions.assertThat(device).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleRuntimeException.class)
    public void reportDeviceTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String deviceToken = "deviceToken";

        //when a review request is made
        sdk.onRequest(request).report(deviceToken);
    }

    @Test(expected = CastleRuntimeException.class)
    public void testExceptionWithServerError () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(500));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String deviceToken = "deviceToken";

        //when a review request is made
        sdk.onRequest(request).report(deviceToken);

    }
}