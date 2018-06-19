package io.castle.client.model;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class CastleMessage {
    private CastleContext context;
    private String createdAt;
    private String deviceToken;
    @Nonnull private String event;
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
        this.event = event;
    }

    public CastleContext getContext() {
        return context;
    }

    public void setContext(CastleContext context) {
        this.context = context;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

        public Builder createdAt(String createdAt) {
            payload.setCreatedAt(createdAt);
            return self();
        }

        public Builder deviceToken(String deviceToken) {
            payload.setDeviceToken(deviceToken);
            return self();
        }

        public Builder reviewId(String reviewId) {
            payload.setReviewId(reviewId);
            return self();
        }

        public Builder properties(Object properties) {
            if (properties == null) {
                throw new NullPointerException("Null properties");
            }
            payload.setProperties(properties);
            return self();
        }

        public Builder other(HashMap other) {
            payload.setOther(other);
            return self();
        }

        public Builder put(String key, Object value) {
            payload.getOther().put(key, value);
            return self();
        }

        public Builder userId(String userId) {
            payload.setUserId(userId);
            return self();
        }

        public Builder userTraits(Object userTraits) {
            if (userTraits == null) {
                throw new NullPointerException("Null userTraits");
            }
            payload.setUserTraits(userTraits);
            return self();
        }

        Builder self() {
            return this;
        }
    }
}
