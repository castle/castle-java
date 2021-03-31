package io.castle.client;

import com.google.gson.JsonParser;
import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.model.*;
import io.castle.client.internal.json.CastleGsonModel;
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

import com.google.common.collect.ImmutableMap;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleAuthenticateHttpTest extends AbstractCastleHttpLayerTest {

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

    private static final String DENY_RESPONSE_NO_POLICY = "{\n" +
                                                "  \"action\": \"deny\",\n" +
                                                "  \"user_id\": \"12345\",\n" +
                                                "  \"device_token\": \"abcdefg1234\"\n" +
                                                "}";

    public CastleAuthenticateHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void authenticateAsyncEndpointWithNullUserIdPayload() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));

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
        sdk.onRequest(request).authenticateAsync(
                CastleMessage.builder("$login.succeeded").build(),
                handler
        );

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    public void authenticateAsyncEndpointWithPayload() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));

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
        sdk.onRequest(request).authenticateAsync(
            CastleMessage.builder("$login.succeeded").userId("12345").build(),
            handler
        );

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test
    public void authenticationAsyncEndpointTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";
        String deviceToken = "abcdefg1234";

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
        String body = recordedRequest.getBody().readUtf8();
        String json = "{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}";

        JSONAssert.assertEquals(json, body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));

        JsonParser parser = new JsonParser();

        RiskPolicyResult riskPolicyResult = new CastleGsonModel().getGson().fromJson("{\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}", RiskPolicyResult.class);

        // and
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId("12345")
                .withDeviceToken(deviceToken)
                .withRiskPolicy(riskPolicyResult)
                .withInternal(parser.parse("{\"action\":\"deny\",\"user_id\":\"12345\",\"device_token\":\"abcdefg1234\", \"risk_policy\": {\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}}"))
                .build();
        waitForValueAndVerify(result, expected);
    }

    @Test
    public void authenticationAsyncEndpointNoRiskPolicyTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE_NO_POLICY));
        String id = "12345";
        String event = "$login.succeeded";
        String deviceToken = "abcdefg1234";

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
        String body = recordedRequest.getBody().readUtf8();
        String json = "{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}";

        JSONAssert.assertEquals(json, body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));

        JsonParser parser = new JsonParser();

        // and
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId("12345")
                .withDeviceToken(deviceToken)
                .withInternal(parser.parse("{\"action\":\"deny\",\"user_id\":\"12345\",\"device_token\":\"abcdefg1234\"}"))
                .build();
        waitForValueAndVerify(result, expected);
    }

    @Test
    public void authenticationAsyncEndpointWithPropertiesAndTraitsTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";
        String deviceToken = "abcdefg1234";

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
        sdk.onRequest(request).authenticateAsync(event, id, ImmutableMap.builder()
                .put("b",0)
                .build(), ImmutableMap.builder()
                .put("y",0)
                .build(), handler);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"properties\":{\"b\":0},\"user_id\":\"12345\",\"user_traits\":{\"y\":0},\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));

        RiskPolicyResult riskPolicyResult = new CastleGsonModel().getGson().fromJson("{\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}", RiskPolicyResult.class);

        // and
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId("12345")
                .withDeviceToken(deviceToken)
                .withRiskPolicy(riskPolicyResult)
                .withInternal(new JsonParser().parse("{\"action\":\"deny\",\"user_id\":\"12345\",\"device_token\":\"abcdefg1234\", \"risk_policy\": {\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}}"))
                .build();
        waitForValueAndVerify(result, expected);
    }


    @Test
    public void authenticationAsyncEndpointProvideDefaultValueOnTimeoutErrorTest() throws InterruptedException, JSONException {
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
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));

        // and
        Verdict expected = VerdictBuilder.failover("timeout")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();

        verifyFailoverResponse(result, expected, false);
    }


    @Test
    public void authenticationEndpointDefaultValueOnTimeoutErrorTest() throws InterruptedException, JSONException {
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
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test
    public void authenticationEndpointWithPayload() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(
            CastleMessage.builder("$login.succeeded").userId("12345").build()
        );

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test
    public void authenticationEndpointTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";
        String deviceToken = "abcdefg1234";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id);

        RiskPolicyResult riskPolicyResult = new CastleGsonModel().getGson().fromJson("{\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}", RiskPolicyResult.class);

        // then
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId(id)
                .withDeviceToken(deviceToken)
                .withRiskPolicy(riskPolicyResult)
                .withInternal(new JsonParser().parse("{\"action\":\"deny\",\"user_id\":\"12345\",\"device_token\":\"abcdefg1234\", \"risk_policy\": {\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}}"))
                .build();
        Assertions.assertThat(verdict).isEqualToComparingFieldByFieldRecursively(expected);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test
    public void authenticationEndpointWithPropertiesAndTraitsTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setBody(DENY_RESPONSE));
        String id = "12345";
        String event = "$login.succeeded";
        String deviceToken = "abcdefg1234";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build(), ImmutableMap.builder()
                .put("x","valueX")
                .put("y",654321)
                .build());

        RiskPolicyResult riskPolicyResult = new CastleGsonModel().getGson().fromJson("{\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}", RiskPolicyResult.class);

        // then
        Verdict expected = VerdictBuilder.success()
                .withAction(AuthenticateAction.DENY)
                .withUserId(id)
                .withDeviceToken(deviceToken)
                .withRiskPolicy(riskPolicyResult)
                .withInternal(new JsonParser().parse("{\"action\":\"deny\",\"user_id\":\"12345\",\"device_token\":\"abcdefg1234\", \"risk_policy\": {\"id\": \"q-rbeMzBTdW2Fd09sbz55A\", \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\"name\": \"Block Users from X\",\"type\": \"bot\"}}"))
                .build();
        Assertions.assertThat(verdict).isEqualToComparingFieldByFieldRecursively(expected);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"properties\":{\"a\":\"valueA\",\"b\":123456},\"user_id\":\"12345\",\"user_traits\":{\"x\":\"valueX\",\"y\":654321},\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test(expected = CastleRuntimeException.class)
    public void authenticationThrowExceptionWhenBackendReturnErrors() throws InterruptedException {
        //given a 403 response from backend
        server.enqueue(new MockResponse().setResponseCode(403));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build(), null);

        // then a exception is send to the client code because of bad response from the castle backend
    }

    @Test
    public void authenticationUseDefaultOnBackendErrorTest() throws InterruptedException, JSONException {
        //given a 403 response from backend
        server.enqueue(new MockResponse().setResponseCode(500));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build(), null);

        // then
        Verdict expected = VerdictBuilder.failover("Client Error")
                .withAction(AuthenticateAction.CHALLENGE)
                .withUserId(id)
                .build();
        verifyFailoverResponse(verdict, expected, false);

        // and
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"event\":\"$login.succeeded\",\"properties\":{\"a\":\"valueA\",\"b\":123456},\"user_id\":\"12345\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +"}}",
                body, false);

        Assert.assertTrue(new JSONObject(body).has("sent_at"));
    }

    @Test(expected = CastleRuntimeException.class)
    public void authenticationUseDefaultOnBadResponseFormatTestEmptyCase() throws InterruptedException {
        //given a response do not match the transport contract
        testIlegalJsonForAuthenticate("{}");

    }
    @Test(expected = CastleRuntimeException.class)
    public void authenticationUseDefaultOnBadResponseFormatTestInvalidActionUseCase() throws InterruptedException {
        //given a response do not match the transport contract
        testIlegalJsonForAuthenticate("{\"action\":\"action\"}");

    }

    private void testIlegalJsonForAuthenticate(String illegalJsonBody) {
        server.enqueue(new MockResponse().setBody(illegalJsonBody));
        String id = "12345";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        Verdict verdict = sdk.onRequest(request).authenticate(event, id, ImmutableMap.builder()
                .put("a","valueA")
                .put("b",123456)
                .build(), null);

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
