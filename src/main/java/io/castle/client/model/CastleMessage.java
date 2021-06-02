package io.castle.client.model;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class CastleMessage {
    private transient CastleContext context;
    private transient CastleOptions options;
    private String createdAt;
    private String timestamp;
    private String deviceToken;
    @Nonnull private String event;
    private String status;
    private String email;
    private Object fingerprint;
    private String ip;
    private CastleHeaders headers;
    /**
     * Collect other attributes that haven't not explicit setters to accommodate for
     * future parameters
     */
    private transient HashMap other;
    private Object properties;
    private String reviewId;
    private String userId;
    private Object userTraits;

    /**
    * Initialize a new request payload message
    * @param  event Name of the event to send
    */
    public CastleMessage(String event) {
        setEvent(event);
    }

    public CastleContext getContext() {
        return context;
    }

    public void setContext(CastleContext context) {
        this.context = context;
    }

    public CastleOptions getOptions() {
        return options;
    }

    public void setOptions(CastleOptions options) { this.options = options; }

    /**
     * @deprecated use {@link #getTimestamp()} instead.
     */
    @Deprecated
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @deprecated use {@link #setTimestamp(String)} instead.
     */
    @Deprecated
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Nonnull
    public String getEvent() {
        return event;
    }

    public void setEvent(@Nonnull String event) {
        this.event = event;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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

    public CastleHeaders getHeaders() { return headers; }

    public void setHeaders(CastleHeaders headers) { this.headers = headers; }

    public String getIp() { return ip; }

    public void setIp(String ip) { this.ip = ip; }

    public HashMap getOther() {
        if (other == null) {
            this.other = new HashMap();
        }
        return other;
    }

    public void setOther(HashMap other) {
        this.other = other;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getUserTraits() {
        return userTraits;
    }

    public void setUserTraits(Object userTraits) {
        this.userTraits = userTraits;
    }

    public static Builder builder(String event) {
        return new Builder(new CastleMessage(event));
    }

    public static class Builder {
        private CastleMessage payload;

        public Builder(CastleMessage payload) {
            this.payload = payload;
        }

        public CastleMessage build() {
            return payload;
        }

        public Builder context(CastleContext context) {
            payload.setContext(context);
            return this;
        }

        /**
         * @deprecated use {@link #timestamp(String)} instead.
         */
        @Deprecated
        public Builder createdAt(String createdAt) {
            payload.setCreatedAt(createdAt);
            return this;
        }

        public Builder timestamp(String timestamp) {
            payload.setTimestamp(timestamp);
            return this;
        }

        public Builder deviceToken(String deviceToken) {
            payload.setDeviceToken(deviceToken);
            return this;
        }

        public Builder reviewId(String reviewId) {
            payload.setReviewId(reviewId);
            return this;
        }

        public Builder properties(Object properties) {
            if (properties == null) {
                throw new NullPointerException("Null properties");
            }
            payload.setProperties(properties);
            return this;
        }

        public Builder other(HashMap other) {
            payload.setOther(other);
            return this;
        }

        public Builder put(String key, Object value) {
            payload.getOther().put(key, value);
            return this;
        }

        public Builder userId(String userId) {
            payload.setUserId(userId);
            return this;
        }

        public Builder status(String status) {
            payload.setStatus(status);
            return this;
        }

        public Builder email(String email) {
            payload.setEmail(email);
            return this;
        }

        public Builder fingerprint(String fingerprint) {
            payload.setFingerprint(fingerprint);
            return this;
        }

        public Builder userTraits(Object userTraits) {
            if (userTraits == null) {
                throw new NullPointerException("Null userTraits");
            }
            payload.setUserTraits(userTraits);
            return this;
        }
    }
}
