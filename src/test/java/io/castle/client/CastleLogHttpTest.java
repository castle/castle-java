package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleResponse;
import io.castle.client.model.generated.Context;
import io.castle.client.model.generated.Log;
import io.castle.client.model.generated.User;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class CastleLogHttpTest extends AbstractCastleHttpLayerTest {

    public CastleLogHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void log() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(204);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        Log log = new Log();
        log.type(Log.TypeEnum._CHALLENGE);
        log.status(Log.StatusEnum.REQUESTED);

        User user = new User();
        user.id("12345");
        log.user(user);

        Context context = new Context();
        context.ip("211.96.77.55");
        context.addHeadersItem("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15");
        log.context(context);

        CastleResponse response = sdk.onRequest(request).log(log);

        if (response.isSuccessful()) {
            // Success
        }

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/log"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }
}
