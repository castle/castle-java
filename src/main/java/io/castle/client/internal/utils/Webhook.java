package io.castle.client.internal.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.security.MessageDigest;

/**
 * Helper for verifying Castle webhook signatures.
 * <p>
 * Castle signs every webhook with an HMAC-SHA256 of the raw request body using the
 * account API secret. The signature is base64 encoded and delivered in the
 * {@code X-Castle-Signature} header.
 */
public final class Webhook {

    private Webhook() {
    }

    /**
     * Computes the base64 encoded HMAC-SHA256 signature for the given body.
     *
     * @param secret the account API secret
     * @param body   the raw request body bytes
     * @return the base64 encoded signature
     */
    public static String computeSignature(String secret, byte[] body) {
        byte[] safeBody = body != null ? body : new byte[0];
        byte[] hmac = Hashing.hmacSha256(secret.getBytes(Charsets.UTF_8))
                .hashBytes(safeBody)
                .asBytes();
        return BaseEncoding.base64().encode(hmac);
    }

    /**
     * Verifies that the supplied signature matches the computed signature for the body.
     * <p>
     * The comparison is done in constant time to avoid timing attacks.
     *
     * @param secret    the account API secret
     * @param body      the raw request body bytes
     * @param signature the signature received in the {@code X-Castle-Signature} header
     * @return {@code true} when the signature matches
     */
    public static boolean verifySignature(String secret, byte[] body, String signature) {
        if (secret == null || signature == null) {
            return false;
        }
        String expected = computeSignature(secret, body);
        return MessageDigest.isEqual(
                expected.getBytes(Charsets.UTF_8),
                signature.getBytes(Charsets.UTF_8)
        );
    }
}
