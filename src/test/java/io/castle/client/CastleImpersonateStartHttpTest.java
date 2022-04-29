package io.castle.client;

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

public class CastleImpersonateStartHttpTest extends AbstractCastleHttpLayerTest {

    public CastleImpersonateStartHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void impersonateStart() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"success\":true}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an devices request is made
        CastleSuccess result = sdk.onRequest(request).impersonateStart("userId");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/impersonate"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());

        // and the correct Success model object is extracted
        CastleSuccess expected = new CastleSuccess();
        expected.setSuccess(true);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test(expected = CastleApiTimeoutException.class)
    public void impersonateStartTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a review request is made
        sdk.onRequest(request).impersonateStart(userId);
    }
}