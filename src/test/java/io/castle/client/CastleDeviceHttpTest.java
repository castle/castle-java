package io.castle.client;

import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.*;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleDeviceHttpTest extends AbstractCastleHttpLayerTest {

    public CastleDeviceHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void getDevice() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(DeviceUtils.getDefaultDeviceJSON());
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an devices request is made
        CastleUserDevice device = sdk.onRequest(request).device("deviceToken");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/devices/deviceToken"), recordedRequest.getRequestUrl());

        // and the correct Review model object is extracted
        CastleUserDevice expected = DeviceUtils.createExpectedDevice();

        Assertions.assertThat(device).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleApiTimeoutException.class)
    public void getDeviceTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String deviceToken = "deviceToken";

        //when a review request is made
        sdk.onRequest(request).device(deviceToken);
    }

    @Test(expected = CastleRuntimeException.class)
    public void testExceptionWithServerError () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(500));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String deviceToken = "deviceToken";

        //when a review request is made
        sdk.onRequest(request).device(deviceToken);

    }

    @Test
    public void getDeviceNotFoundTest () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(404));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String deviceToken = "deviceToken";

        //when a review request is made
        CastleUserDevice userDevice = sdk.onRequest(request).device(deviceToken);

        Assert.assertNull(userDevice);
    }
}