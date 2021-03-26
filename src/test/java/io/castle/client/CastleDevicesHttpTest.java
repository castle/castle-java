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

public class CastleDevicesHttpTest extends AbstractCastleHttpLayerTest {

    private CastleGsonModel model = new CastleGsonModel();

    public CastleDevicesHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void getDevices() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"total_count\":1,\"data\":[" + DeviceUtils.getDefaultDeviceJSON() + "]}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an devices request is made
        CastleUserDevices devices = sdk.onRequest(request).userDevices("userId");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/users/userId/devices"), recordedRequest.getRequestUrl());

        // and the correct CastleUserDevice model object is extracted
        CastleUserDevices expected = DeviceUtils.createExpectedDevices();

        Assertions.assertThat(devices).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleApiTimeoutException.class)
    public void getDevicesTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a v1/users/:user_id/devices request is made
        sdk.onRequest(request).userDevices(userId);
    }

    @Test(expected = CastleRuntimeException.class)
    public void testExceptionWithServerError () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(500));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a v1/users/:user_id/devices request is made
        sdk.onRequest(request).userDevices(userId);

    }

    @Test
    public void getDevicesNotFoundTest () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(404));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a v1/users/:user_id/devices request is made
        CastleUserDevices userDevices = sdk.onRequest(request).userDevices(userId);

        Assert.assertNull(userDevices);
    }
}
