package io.castle.client;

import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleHeaders;
import io.castle.client.utils.SDKVersion;
import io.castle.client.model.CastleMessage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.collect.ImmutableMap;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

public class CastleTrackHttpTest extends AbstractCastleHttpLayerTest {

    public CastleTrackHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void trackWithManualContext() throws Exception {
        server.enqueue(new MockResponse());

        Castle.setSingletonInstance(sdk);

        // When
        CastleContext context = Castle.instance().contextBuilder()
            .ip("1.1.1.1")
            .userAgent("Mozilla/5.0")
            .clientId("")
            .headers(CastleHeaders.builder()
                .add("User-Agent", "Mozilla/5.0")
                .add("Accept-Language", "sv-se")
                .build()
            )
            .build();

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an track request is made
        sdk.onRequest(request).track(CastleMessage.builder("$login.succeeded")
            .userId("12345")
            .context(context)
            .build()
        );

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"1.1.1.1\",\"headers\":{\"User-Agent\":\"Mozilla/5.0\",\"Accept-Language\":\"sv-se\"}," + SDKVersion.getLibraryString() +",\"user_agent\":\"Mozilla/5.0\"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointHttpAuthorize() throws Exception {
        // Given
        Castle.verifySdkConfigurationAndInitialize();

        server.enqueue(new MockResponse());

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an track request is made
        sdk.onRequest(request).track("$login.successful", "1234");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("Basic OnRlc3RfYXBpX3NlY3JldA==", recordedRequest.getHeader("Authorization"));
    }

    @Test
    public void trackEndpointWithPaylod() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse());
        CastleMessage payload = CastleMessage.builder("$login.succeeded")
            .userId("12345")
            .build();
        HttpServletRequest request = new MockHttpServletRequest();

        // When
        sdk.onRequest(request).track(payload);

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointWithUserIDTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse());
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an track request is made
        sdk.onRequest(request).track(event, id);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointWithUserIDAndReviewIdTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse());
        String userId = "12345";
        String reviewId = "r45677";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an track request is made
        sdk.onRequest(request).track(event, userId,reviewId);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"review_id\":\"r45677\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);

    }

    @Test
    public void trackEndpointWithUserIDAndPropertiesTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse());
        String userId = "12345";
        String reviewId = "r987";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an track request is made
        sdk.onRequest(request).track(event, userId, reviewId, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build());

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"properties\":{\"a\":\"valueA\",\"b\":123456},\"review_id\":\"r987\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointWithUserIDAndPropertiesAndTraitTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse());
        String userId = "23456";
        String reviewId = "r987";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an track request is made
        sdk.onRequest(request).track(event, userId, reviewId, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build(), ImmutableMap.builder()
                .put("x", "x value")
                .put("y", 2342)
                .build());

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"properties\":{\"a\":\"valueA\",\"b\":123456},\"review_id\":\"r987\",\"user_id\":\"23456\",\"user_traits\":{\"x\":\"x value\",\"y\":2342},\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointWithMinimalInformationTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse());
        String event = "any.valid.event";
        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an track request is made
        sdk.onRequest(request).track(event);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"any.valid.event\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void trackEndpointAsyncTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String event = "any.valid.event";
        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an async track request is made
        final AtomicReference<Boolean> result = new AtomicReference<>();
        AsyncCallbackHandler<Boolean> callback = new AsyncCallbackHandler<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                result.set(response);
            }

            @Override
            public void onException(Exception exception) {
                Assertions.fail("No exception expected here");
            }
        };
        sdk.onRequest(request).track(event, null, null, null,null,callback);

        // then the callback response is called with true
        Boolean extracted = waitForValue(result);
        Assertions.assertThat(extracted).isTrue();
    }

    @Test
    public void trackEndpointTimeoutTest() throws InterruptedException, JSONException {
        // given the backend will timeout
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE));
        String event = "any.valid.event";
        // and a mock Request
        final HttpServletRequest request = new MockHttpServletRequest();

        // and a async handler is prepared
        final AtomicReference<Boolean> result = new AtomicReference<>();
        AsyncCallbackHandler<Boolean> callback = new AsyncCallbackHandler<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                Assertions.fail("Request should end with an timeout exception and not result");
            }

            @Override
            public void onException(Exception exception) {
                result.set(false);
            }
        };
        // when an track request is made
        sdk.onRequest(request).track(event, null, null, null,null,callback);

        // then the track request must be send
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"any.valid.event\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);

        // and the onException method must be called
        waitForValueAndVerify(result, Boolean.FALSE);
    }

    @Test
    public void trackEndpointTimeoutAreIgnoreWhenNoCallbackIsProvidedTest() throws InterruptedException, JSONException {
        // given the backend will timeout
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE));
        String event = "any.valid.event";
        // and a mock Request
        final HttpServletRequest request = new MockHttpServletRequest();

        // when an track request is made
        sdk.onRequest(request).track(event, null, null, null);

        // then the track request must be send
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"event\":\"any.valid.event\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);

        // and no exceptions are thrown in any thread
    }

}