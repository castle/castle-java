package io.castle.client;

import io.castle.client.model.*;
import okhttp3.mockwebserver.MockResponse;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleThrowStrategyTest extends AbstractCastleHttpLayerTest {

    public CastleThrowStrategyTest() {
        super(new AuthenticateFailoverStrategy());
    }

    @Test(expected = CastleApiTimeoutException.class)
    public void onThrowStrategyAuthenticateThrowCastleRuntimeExceptionWhenNoResponse() {
        // given the throw strategy is setup (see constructor)
        // and backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        // and
        String id = "12345";
        String event = "$login.succeeded";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when authenticate request is made
        sdk.onRequest(request).authenticate(event, id);

        // then the exception is throw
    }

    @Test(expected = CastleApiInternalServerErrorException.class)
    public void onThrowStrategyAuthenticateThrowCastleRuntimeExceptionWhenResponseIs500() {
        // given the throw strategy is setup (see constructor)
        // and backend request timeouts
        server.enqueue(new MockResponse().setResponseCode(500));
        // and
        String id = "12345";
        String event = "$login.succeeded";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when authenticate request is made
        Verdict authenticate = sdk.onRequest(request).authenticate(event, id);

        // then the exception is throw
        Assertions.fail("A exception should be throw");
    }

    @Test
    public void onThrowStrategyAuthenticateAsyncThrowCastleRuntimeException() {
        // given the throw strategy is setup (see constructor)
        // and backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        // and
        String id = "12345";
        String event = "$login.succeeded";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        // and a async callback is prepared
        final AtomicReference<Exception> exceptionAtomicReference = new AtomicReference<>();
        AsyncCallbackHandler<Verdict> callback = new AsyncCallbackHandler<Verdict>() {
            @Override
            public void onResponse(Verdict response) {
                Assertions.fail("A exceptions was expected");
            }

            @Override
            public void onException(Exception exception) {
                exceptionAtomicReference.set(exception);
            }
        };
        // when authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, callback);

        // then the exception is throw in the callback
        Exception exception = waitForValue(exceptionAtomicReference);
        Assertions.assertThat(exception)
                .isNotNull()
                .isInstanceOf(CastleRuntimeException.class);
    }


}
