package io.castle.client;

import io.castle.client.internal.utils.Webhook;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CastleWebhookTest {

    private static final String SECRET = "test_api_secret";

    @Test
    public void verifiesValidSignature() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(StandardCharsets.UTF_8);
        String signature = Webhook.computeSignature(SECRET, body);

        Assertions.assertThat(sdk.verifyWebhookSignature(signature, body)).isTrue();
    }

    @Test
    public void rejectsInvalidSignature() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(StandardCharsets.UTF_8);

        Assertions.assertThat(sdk.verifyWebhookSignature("not-the-signature", body)).isFalse();
    }

    @Test
    public void rejectsNullSignature() throws CastleSdkConfigurationException {
        Castle sdk = Castle.verifySdkConfigurationAndInitialize();
        byte[] body = "{}".getBytes(StandardCharsets.UTF_8);

        Assertions.assertThat(sdk.verifyWebhookSignature((String) null, body)).isFalse();
    }
}
