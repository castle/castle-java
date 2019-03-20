package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleRuntimeException;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleImpersonateEndHttpTest extends AbstractCastleHttpLayerTest {

    public CastleImpersonateEndHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void impersonateEnd() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"success\":true}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an devices request is made
        String result = sdk.onRequest(request).impersonateEnd("userId");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/impersonate"), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());

        // and the correct Review model object is extracted
        //CastleUserDevice expected = DeviceUtils.createExpectedDevice();

        //Assertions.assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleRuntimeException.class)
    public void impersonateEndTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a review request is made
        sdk.onRequest(request).impersonateStart(userId);
    }
}