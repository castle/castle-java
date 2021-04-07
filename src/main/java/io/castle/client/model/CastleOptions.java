package io.castle.client.model;

/**
 * Complete model of default request options
 */
public class CastleOptions {
    private Object fingerprint;
    private String ip;
    private CastleHeaders headers;

    public Object getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public void setFingerprint(Object fingerprint) {
        if (fingerprint instanceof Boolean || fingerprint instanceof String) {
            this.fingerprint = fingerprint;
        } else {
            throw new IllegalArgumentException("Fingerprint must be a string or boolean value");
        }
    }

    public void setFingerprint(boolean fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public CastleHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(CastleHeaders headers) {
        this.headers = headers;
    }

    /**
     * Produces a string representation of the options object.
     *
     * @return the string representation of this {@code castleOptions}
     */
    @Override
    public String toString() {
        return "CastleOptions{" +
                "fingerprint='" + fingerprint + '\'' +
                ", headers=" + headers +
                ", ip='" + ip + '\'' +
                '}';
    }
}
