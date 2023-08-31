package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleApiInternalServerErrorException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CastleRemoveUserHttpTest extends AbstractCastleHttpLayerTest {

    public CastleRemoveUserHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void removeUser() throws Exception {
        // Mock a response
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(202);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an privacy remove user request is made
        Boolean response = sdk.onRequest(request).removeUser("user-test-id");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users/user-test-id"), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
        Assert.assertTrue(response);
    }

    @Test
    public void removeUserNotFoundTest() throws Exception {
        // Mock a response
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(404);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an privacy remove user request is made
        Boolean response = sdk.onRequest(request).removeUser("user-test-id");

        // Then it doesn't throw exception
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users/user-test-id"), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());

        Assert.assertNull(response);
    }

    @Test(expected = CastleApiInternalServerErrorException.class)
    public void removeUserServerError() {
        // Mock a response
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(500);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And a privacy remove user request is made, exception throw
        Boolean response = sdk.onRequest(request).removeUser("user-test-id");

        Assert.assertNull(response);
    }
}