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

public class CastlePrivacyHttpTest extends AbstractCastleHttpLayerTest {

    @Test
    public void requestUserData() throws InterruptedException, JSONException {
        server.enqueue(new MockResponse().setBody("{}"));
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).requestUserData(ImmutableMap.builder()
                .put("identifier", "12345")
                .put("identifier_type", "$id")
                .build());
        if (response == null) {
            Assertions.fail("error on request");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
        JSONAssert.assertEquals("{\"identifier\":\"12345\",\"identifier_type\":\"$id\"}", body, false);
    }

    @Test
    public void deleteUserData() throws InterruptedException, JSONException {
        server.enqueue(new MockResponse().setBody("{}"));
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).deleteUserData(ImmutableMap.builder()
                .put("identifier", "12345")
                .put("identifier_type", "$id")
                .build());
        if (response == null) {
            Assertions.fail("error on request");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users"), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
        JSONAssert.assertEquals("{\"identifier\":\"12345\",\"identifier_type\":\"$id\"}", body, false);
    }
}
