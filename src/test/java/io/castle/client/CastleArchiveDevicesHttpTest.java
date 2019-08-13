package io.castle.client;

import io.castle.client.model.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleArchiveDevicesHttpTest extends AbstractCastleHttpLayerTest {

    public CastleArchiveDevicesHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void archiveDevices() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"id\":\"userId\",\"created_at\":null,\"updated_at\":\"2019-03-26T11:31:19.968Z\",\"last_seen_at\":\"2019-03-26T09:38:43.339Z\",\"flagged_at\":null,\"risk\":0.0,\"leaks_count\":3,\"devices_count\":2,\"email\":\"admin@example.com\",\"name\":\"Doe\",\"username\":null,\"phone\":null,\"address\":{\"street\":null,\"city\":null,\"postal_code\":null,\"region\":null,\"country\":null},\"custom_attributes\":{}}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an devices request is made
        CastleUser result = sdk.onRequest(request).archiveUserDevices("userId");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/users/userId/archive_devices"), recordedRequest.getRequestUrl());
        Assert.assertEquals("PUT", recordedRequest.getMethod());

        // and the correct Success model object is extracted
        CastleUser expected = new CastleUser();
        expected.setId("userId");
        expected.setUpdatedAt("2019-03-26T11:31:19.968Z");
        expected.setLastSeenAt("2019-03-26T09:38:43.339Z");
        expected.setLeaksCount(3);
        expected.setDevicesCount(2);
        expected.setEmail("admin@example.com");
        expected.setName("Doe");
        expected.setAddress(new CastleUserAddress());
        expected.setCustomAttributes(new HashMap<String, String>());

        Assertions.assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleRuntimeException.class)
    public void archiveDevicesTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a review request is made
        sdk.onRequest(request).archiveUserDevices(userId);
    }

    @Test
    public void archiveDevicesNotFoundTest () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(404));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String userId = "userId";

        //when a review request is made
        CastleUser user = sdk.onRequest(request).archiveUserDevices(userId);

        Assert.assertNull(user);
    }
}