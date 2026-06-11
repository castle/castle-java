package io.castle.client.servlet;

import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.Webhook;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleHeaders;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CastleServletContextTest {

    private static final String SECRET = "test_api_secret";

    private final CastleGsonModel model = new CastleGsonModel();

    private CastleConfiguration configuration() throws CastleSdkConfigurationException {
        return CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
    }

    @Test
    public void buildsContextFromServletRequest() throws CastleSdkConfigurationException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("8.8.8.8");
        request.addHeader("User-Agent", "agent");
        request.addHeader("Accept", "text/html");

        CastleContext context = new CastleServletContext(configuration(), model).toContext(request);

        Assertions.assertThat(context.getIp()).isEqualTo("8.8.8.8");
        Assertions.assertThat(context.getUserAgent()).isEqualTo("agent");
        Assertions.assertThat(context.getClientId()).isEqualTo(false);
    }

    @Test
    public void resolvesIpFromConfiguredHeaders() throws CastleSdkConfigurationException {
        CastleConfiguration configuration = CastleConfigurationBuilder.defaultConfigBuilder()
                .apiSecret("abcd")
                .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("8.8.8.8");
        request.addHeader("X-Forwarded-For", "1.1.1.1,2.2.2.2");
        request.addHeader("CF-Connecting-IP", "4.4.4.4");

        CastleContext context = new CastleServletContext(configuration, model).toContext(request);

        Assertions.assertThat(context.getIp()).isEqualTo("1.1.1.1,2.2.2.2");
    }

    @Test
    public void servletAndManualBuildersProduceSameContext() throws CastleSdkConfigurationException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("8.8.8.8");
        request.addHeader("User-Agent", "agent");

        CastleContext servletContext = new CastleServletContext(configuration(), model).toContext(request);

        CastleHeaders headers = CastleHeaders.builder()
                .add("User-Agent", "agent")
                .add("REMOTE_ADDR", "8.8.8.8")
                .build();
        CastleContext manualContext = new io.castle.client.internal.utils.CastleContextBuilder(configuration(), model)
                .userAgent("agent")
                .headers(headers)
                .ip("8.8.8.8")
                .clientId(false)
                .build();

        Assertions.assertThat(servletContext).usingRecursiveComparison().isEqualTo(manualContext);
    }

    @Test
    public void verifiesWebhookSignatureFromServletRequest() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(StandardCharsets.UTF_8);
        String signature = Webhook.computeSignature(SECRET, body);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(Castle.WEBHOOK_SIGNATURE_HEADER, signature);

        Assertions.assertThat(CastleServletContext.verifyWebhookSignature(sdk, request, body)).isTrue();
        Assertions.assertThat(sdk.verifyWebhookSignature(request, body)).isTrue();
    }

    @Test
    public void rejectsNullServletRequest() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        Assertions.assertThat(CastleServletContext.verifyWebhookSignature(sdk, null, new byte[0])).isFalse();
    }
}
