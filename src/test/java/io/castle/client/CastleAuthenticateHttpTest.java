package io.castle.client;

import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.model.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleAuthenticateHttpTest extends AbstractCastleHttpLayerTest {

    public CastleAuthenticateHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

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
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}}}",
                recordedRequest.getBody().readUtf8());

        // and
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId("12345")
                .build();
        waitForValueAndVerify(result, expected);
    }

    @Test
    public void authenticationAsyncEndpointWithPropertiesAndTraitsTest() throws InterruptedException {
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
        CustomAppProperties properties = new CustomAppProperties();
        CustomAppTraits traits = new CustomAppTraits();
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, properties, traits, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}},\"properties\":{\"b\":0},\"traits\":{\"y\":0}}",
                recordedRequest.getBody().readUtf8());

        // and
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId("12345")
                .build();
        waitForValueAndVerify(result, expected);
    }


    @Test
    public void authenticationAsyncEndpointProvideDefaultValueOnTimeoutErrorTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
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
        // and an authenticate request is made
        sdk.onRequest(request).authenticateAsync(event, id, handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}}}",
                recordedRequest.getBody().readUtf8());

        // and
        Verdict expected = VerdictBuilder.failover("timeout")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();

        verifyFailoverResponse(result, expected, false);
    }


    @Test
    public void authenticationEndpointDefaultValueOnTimeoutErrorTest() throws InterruptedException {
        //given the backed will timeout
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id);

        // then
        Verdict expected = VerdictBuilder.failover("timeout")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();
        verifyFailoverResponse(verdict, expected, false);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}}}",
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
        Verdict verdict = sdk.onRequest(request).authenticate(event, id);

        // then
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId(id)
                .build();
        Assertions.assertThat(verdict).isEqualToComparingFieldByField(expected);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void authenticationEndpointWithPropertiesAndTraitsTest() throws InterruptedException {
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
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        CustomAppTraits traits = new CustomAppTraits();
        traits.setX("valueX");
        traits.setY(654321);

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, properties, traits);

        // then
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId(id)
                .build();
        Assertions.assertThat(verdict).isEqualToComparingFieldByField(expected);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}},\"properties\":{\"a\":\"valueA\",\"b\":123456},\"traits\":{\"x\":\"valueX\",\"y\":654321}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test(expected = CastleRuntimeException.class)
    public void authenticationThrowExceptionWhenBackendReturnErrors() throws InterruptedException {
        //given a 403 response from backend
        server.enqueue(new MockResponse().setResponseCode(403));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, properties, null);

        // then a exception is send to the client code because of bad response from the castle backend
    }

    @Test
    public void authenticationUseDefaultOnBackendErrorTest() throws InterruptedException {
        //given a 403 response from backend
        server.enqueue(new MockResponse().setResponseCode(500));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, properties, null);

        // then
        Verdict expected = VerdictBuilder.failover("Client Error")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();
        verifyFailoverResponse(verdict, expected, false);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"name\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true,\"client_id\":\"\",\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"castle-java\",\"version\":\"1.0.1\"}},\"properties\":{\"a\":\"valueA\",\"b\":123456}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test(expected = CastleRuntimeException.class)
    public void authenticationUseDefaultOnBadResponseFormatTestEmptyCase() throws InterruptedException {
        //given a response do not match the transport contract
        testIlegalJsonForAuthenticate("{}");

    }
    @Test(expected = CastleRuntimeException.class)
    public void authenticationUseDefaultOnBadResponseFormatTestMissingUseCase() throws InterruptedException {
        //given a response do not match the transport contract
        testIlegalJsonForAuthenticate("{\"action\":\"deny\"}");

    }

    private void testIlegalJsonForAuthenticate(String illegalJsonBody) {
        server.enqueue(new MockResponse().setBody(illegalJsonBody));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, properties, null);

        // then a illegal json failover is provided
        Verdict expected = VerdictBuilder.failover("Illegal json format")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();
        verifyFailoverResponse(verdict, expected, true);
    }

    /**
     * Verify that the async Verdict value match the expected values.
     * The Failover reason depend from the JVM implementation, so we only check that is not null.
     *
     * @param result
     * @param expected
     */
    private void verifyFailoverResponse(AtomicReference<Verdict> result, Verdict expected, boolean expectedExactReasonMatch) {
        Verdict extractedVerdict = waitForValue(result);
        verifyFailoverResponse(extractedVerdict, expected, expectedExactReasonMatch);
    }

    /**
     * Sync version of the verification for Verdict
     *
     * @param extractedVerdict
     * @param expected
     */
    private void verifyFailoverResponse(Verdict extractedVerdict, Verdict expected, boolean expectedExactReasonMatch) {
        Assertions.assertThat(extractedVerdict.getAction()).isEqualTo(expected.getAction());
        Assertions.assertThat(extractedVerdict.getUserId()).isEqualTo(expected.getUserId());
        Assertions.assertThat(extractedVerdict.isFailover()).isTrue();
        if (expectedExactReasonMatch) {
            Assertions.assertThat(extractedVerdict.getFailoverReason()).isEqualTo(expected.getFailoverReason());
        } else {
            Assertions.assertThat(extractedVerdict.getFailoverReason()).isNotEmpty();
        }
    }
}