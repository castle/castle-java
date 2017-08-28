package io.castle.client;

import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleAuthenticateHttpTest extends AbstractCastleHttpLayerTest {

    @Test
    public void authenticationAsyncEndpointTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        final AtomicReference<AuthenticateAction> result = new AtomicReference<>();
        AsyncCallbackHandler<AuthenticateAction> handler = new AsyncCallbackHandler<AuthenticateAction>() {
            @Override
            public void onResponse(AuthenticateAction response) {
                result.set(response);
            }
        };
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());

        // and
        waitForValueAndVerify(result, AuthenticateAction.DENY);
    }

    @Test
    public void authenticationAsyncEndpointWithPropertiesTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        final AtomicReference<AuthenticateAction> result = new AtomicReference<>();
        AsyncCallbackHandler<AuthenticateAction> handler = new AsyncCallbackHandler<AuthenticateAction>() {
            @Override
            public void onResponse(AuthenticateAction response) {
                result.set(response);
            }
        };
        CustomAppProperties properties = new CustomAppProperties();
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, properties, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"properties\":{\"b\":0}}",
                recordedRequest.getBody().readUtf8());

        // and
        waitForValueAndVerify(result, AuthenticateAction.DENY);
    }


    @Test
    public void authenticationAsyncEndpointProvideDefaultValueOnTimeoutErrorTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        final AtomicReference<AuthenticateAction> result = new AtomicReference<>();
        AsyncCallbackHandler<AuthenticateAction> handler = new AsyncCallbackHandler<AuthenticateAction>() {
            @Override
            public void onResponse(AuthenticateAction response) {
                result.set(response);
            }
        };
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());

        // and
        waitForValueAndVerify(result, AuthenticateAction.CHALLENGE);
    }


    @Test
    public void authenticationEndpointDefaultValueOnTimeoutErrorTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id);

        // then
        Assert.assertEquals(AuthenticateAction.CHALLENGE, authenticateAction);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void authenticationEndpointTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setBody("\n" +
                "{\n" +
                "  \"action\": \"deny\",\n" +
                "  \"user_id\": \"12345\"\n" +
                "}"));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id);

        // then
        Assert.assertEquals(AuthenticateAction.DENY, authenticateAction);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void authenticationEndpointWithPropertiesTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setBody("\n" +
                "{\n" +
                "  \"action\": \"deny\",\n" +
                "  \"user_id\": \"12345\"\n" +
                "}"));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id, properties);

        // then
        Assert.assertEquals(AuthenticateAction.DENY, authenticateAction);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"properties\":{\"a\":\"valueA\",\"b\":123456}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void authenticationUseDefaultOnErrorTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(403));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id, properties);

        // then
        Assert.assertEquals(AuthenticateAction.CHALLENGE, authenticateAction);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"properties\":{\"a\":\"valueA\",\"b\":123456}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void authenticationUseDefaultOnBadResponseFormatTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setBody("{}"));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id, properties);

        // then
        Assert.assertEquals(AuthenticateAction.CHALLENGE, authenticateAction);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"properties\":{\"a\":\"valueA\",\"b\":123456}}",
                recordedRequest.getBody().readUtf8());
    }

}