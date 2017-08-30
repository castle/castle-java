package io.castle.client;

import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleRuntimeException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleThrowStrategy extends AbstractCastleHttpLayerTest {

    public CastleThrowStrategy() {
        super(new AuthenticateFailoverStrategy());
    }

    @Test(expected = CastleRuntimeException.class)
    public void onThrowStrategyAuthenticateThrowCastleRuntimeException() {
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

}
