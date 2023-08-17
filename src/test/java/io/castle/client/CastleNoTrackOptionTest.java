package io.castle.client;

import io.castle.client.api.CastleApi;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.Verdict;
import okhttp3.mockwebserver.MockResponse;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

public class CastleNoTrackOptionTest extends AbstractCastleHttpLayerTest {

    public CastleNoTrackOptionTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void noTrackOptionDisableRequests() throws InterruptedException {

        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(true);
        // And a custom handler for the async authenticate call
        final AtomicReference<Verdict> authenticateAsyncResult = new AtomicReference<>();
        AsyncCallbackHandler<Verdict> handler = new AsyncCallbackHandler<Verdict>() {
            @Override
            public void onResponse(Verdict response) {
                authenticateAsyncResult.set(response);
            }

            @Override
            public void onException(Exception exception) {
                Assertions.fail("error on request", exception);
            }
        };

        //When all API call are executed
        castleApi.track("testEvent");
        Verdict verdict = castleApi.authenticate("testEvent", "userId");
        castleApi.authenticateAsync("testEvent", "userId", handler);

        //and we await the timeout period to avoid concurrency races
        Thread.sleep(120);

        //Then no calls are made to the backend
        Assertions.assertThat(server.getRequestCount()).isEqualTo(0);
        //And the Verdict is the default ALLOW value
        Assert.assertEquals(AuthenticateAction.ALLOW, verdict.getAction());
        Assert.assertEquals("userId", verdict.getUserId());
        Assert.assertEquals(AuthenticateAction.ALLOW, authenticateAsyncResult.get().getAction());
        Assertions.assertThat(authenticateAsyncResult.get().isFailover()).isTrue();
    }

    @Test
    public void useCustomTrackHandlerWithNoTrackOptionToDisableRequests() {
        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(true);
        // And a custom handler for the async track call
        final AtomicReference<Boolean> trackAsyncResult = new AtomicReference<>();

        AsyncCallbackHandler<Boolean> trackHandler = new AsyncCallbackHandler<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                trackAsyncResult.set(response);
            }

            @Override
            public void onException(Exception exception) {
                Assertions.fail("error on request", exception);

            }
        };

        //When an API call is executed
        castleApi.track(
                "testEvent",
                null,
                null,
                null,
                null,
                trackHandler
        );

        //Then no calls are made to the backend
        Assertions.assertThat(server.getRequestCount()).isEqualTo(0);

        //And the track call returns and the resulting boolean is true
        Assert.assertTrue(trackAsyncResult.get());
    }

    @Test
    public void noTrackOptionCreateAllowFailoverVerdict() {

        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(true);
        //When authenticate is called

        Verdict verdict = castleApi.authenticate("testEvent", "userId");

        //Then no calls are made to the backend
        Assertions.assertThat(server.getRequestCount()).isEqualTo(0);
        //And the Verdict is the correct failover value
        Assert.assertEquals(AuthenticateAction.ALLOW, verdict.getAction());

        Assertions.assertThat(verdict.getUserId()).isEqualTo("userId");
        Assertions.assertThat(verdict.getAction()).isEqualTo(AuthenticateAction.ALLOW);
        Assertions.assertThat(verdict.getFailoverReason()).isEqualTo("Castle set to do not track.");
        Assertions.assertThat(verdict.isFailover()).isTrue();
    }


    @Test
    public void trackOptionAllowRequests() throws InterruptedException {

        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And responses are setup on mock server
        server.enqueue(new MockResponse().setResponseCode(200));
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(false);
        // And a custom handler for the async authenticate call
        final AtomicReference<Verdict> result = new AtomicReference<>();
        AsyncCallbackHandler<Verdict> handler = new AsyncCallbackHandler<Verdict>() {
            @Override
            public void onResponse(Verdict response) {
                result.set(response);
            }

            @Override
            public void onException(Exception exception) {
                Assertions.fail("error on request", exception);
            }
        };

        //When all API call are executed
        castleApi.track("testEvent");
        server.takeRequest();
        castleApi.authenticate("testEvent", "userId");
        server.takeRequest();
        castleApi.authenticateAsync("testEvent", "userId", handler);
        server.takeRequest();

        //Then all the calls are executed
        waitForValue(result);
        Assertions.assertThat(server.getRequestCount()).isEqualTo(3);
        Assert.assertEquals(AuthenticateAction.DENY, result.get().getAction());
    }

}
