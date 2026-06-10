package io.castle.client;

import com.google.common.base.Charsets;
import io.castle.client.internal.utils.Webhook;
import io.castle.client.model.CastleSdkConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class CastleWebhookTest {

    private static final String SECRET = "test_webhook_secret";

    private Castle sdk;

    @Before
    public void setUp() throws CastleSdkConfigurationException {
        sdk = Castle.initialize(SECRET);
    }

    @Test
    public void verifiesValidSignature() {
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(Charsets.UTF_8);
        String signature = Webhook.computeSignature(SECRET, body);

        Assert.assertTrue(sdk.verifyWebhookSignature(signature, body));
    }

    @Test
    public void rejectsInvalidSignature() {
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(Charsets.UTF_8);

        Assert.assertFalse(sdk.verifyWebhookSignature("not-the-signature", body));
    }

    @Test
    public void rejectsNullSignature() {
        byte[] body = "{}".getBytes(Charsets.UTF_8);

        Assert.assertFalse(sdk.verifyWebhookSignature((String) null, body));
    }

    @Test
    public void verifiesSignatureFromServletRequest() {
        byte[] body = "{\"type\":\"$review.opened\"}".getBytes(Charsets.UTF_8);
        String signature = Webhook.computeSignature(SECRET, body);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(Castle.WEBHOOK_SIGNATURE_HEADER, signature);

        Assert.assertTrue(sdk.verifyWebhookSignature(request, body));
    }

    @Test
    public void computesStableSignature() {
        byte[] body = "payload".getBytes(Charsets.UTF_8);

        String first = Webhook.computeSignature(SECRET, body);
        String second = Webhook.computeSignature(SECRET, body);

        Assert.assertEquals(first, second);
        Assert.assertFalse(first.isEmpty());
    }
}
