package io.castle.client;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.Timestamp;
import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.model.*;
import io.castle.client.utils.DeviceUtils;
import io.castle.client.utils.SDKVersion;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleGenericAPIHttpTest extends AbstractCastleHttpLayerTest {

    private static final String DENY_RESPONSE = "{\n" +
                                                "  \"action\": \"deny\",\n" +
                                                "  \"user_id\": \"12345\",\n" +
                                                "  \"device_token\": \"abcdefg1234\",\n" +
                                                "  \"risk_policy\": {\n" +
                                                "    \"id\": \"q-rbeMzBTdW2Fd09sbz55A\",\n" +
                                                "    \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\n" +
                                                "    \"name\": \"Block Users from X\",\n" +
                                                "    \"type\": \"bot\"\n" +
                                                "  }\n" +
                                                "}";

    public CastleGenericAPIHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void postRequest() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        CastleContext payload = sdk.contextBuilder()
                .build();

        // and an authenticate request is made
        CastleResponse response = sdk.onRequest(request).post("/v1/authenticate", new ImmutableMap.Builder<String, Object>()
                .put("event", "$login.succeeded")
                .put("userId", "12345")
                .put("context", payload)
                .put("sent_at", Timestamp.timestamp())
                .build());

        if (response == null) {
            Assertions.fail("error on request");
        }

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test
    public void putRequest() throws InterruptedException, JSONException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // Request is made
        CastleResponse response = sdk.onRequest(request).put("/v1/test", new ImmutableMap.Builder<String, Object>()
                .put("event", "test")
                .put("userId", "12345")
                .build());

        if (response == null) {
            Assertions.fail("error on request");
        }

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/test"), recordedRequest.getRequestUrl());
        JSONAssert.assertEquals("{\"event\":\"test\",\"userId\":\"12345\"}", body, false);
        Assert.assertEquals("PUT", recordedRequest.getMethod());
    }

    @Test
    public void getRequest() throws InterruptedException, JSONException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // Request is made
        CastleResponse response = sdk.onRequest(request).get("/v1/test");

        if (response == null) {
            Assertions.fail("error on request");
        }

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/test"), recordedRequest.getRequestUrl());
        Assert.assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    public void deleteRequest() throws InterruptedException, JSONException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{}");
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // Request is made
        CastleResponse response = sdk.onRequest(request).delete("/v1/test", new ImmutableMap.Builder<String, Object>()
                .put("event", "test")
                .put("userId", "12345")
                .build());

        if (response == null) {
            Assertions.fail("error on request");
        }

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/test"), recordedRequest.getRequestUrl());
        JSONAssert.assertEquals("{\"event\":\"test\",\"userId\":\"12345\"}", body, false);
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
    }
}
