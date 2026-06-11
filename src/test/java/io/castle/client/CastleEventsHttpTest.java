package io.castle.client;

import com.google.common.collect.ImmutableMap;
import io.castle.client.model.CastleResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CastleEventsHttpTest extends AbstractCastleHttpLayerTest {

    @Test
    public void eventsSchema() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).eventsSchema();
        if (response == null) {
            Assertions.fail("error on request");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/schema"), recordedRequest.getRequestUrl());
        Assert.assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    public void queryEvents() throws InterruptedException, JSONException {
        server.enqueue(new MockResponse().setBody("{}"));
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).queryEvents(ImmutableMap.builder()
                .put("type", "$login")
                .build());
        if (response == null) {
            Assertions.fail("error on request");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/query"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
        JSONAssert.assertEquals("{\"type\":\"$login\"}", body, false);
    }

    @Test
    public void groupEvents() throws InterruptedException, JSONException {
        server.enqueue(new MockResponse().setBody("{}"));
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).groupEvents(ImmutableMap.builder()
                .put("field", "name")
                .build());
        if (response == null) {
            Assertions.fail("error on request");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/group"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
        JSONAssert.assertEquals("{\"field\":\"name\"}", body, false);
    }
}
