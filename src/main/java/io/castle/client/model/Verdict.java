package io.castle.client.model;

import com.google.gson.JsonElement;

/**
 * Model of the outcome of an authenticate call to the Castle API.
 */
public class Verdict {

    /**
     * AuthenticateAction returned by a call to the CastleAPI, or configured by a failover strategy.
     */
    private AuthenticateAction action;

    /**
     * String representing a user ID associated with an authenticate call.
     */
    private String userId;

    /**
     * True if the SDK resorted the {@code AuthenticateFailoverStrategy} configured.
     */
    private boolean failover;

    /**
     * Explains the reason why the {@code AuthenticateFailoverStrategy} was used.
     */
    private String failoverReason;

    /**
     * String representing a device ID associated with an authenticate call.
     */
    private String deviceToken;

    /**
     * JsonElement representing the full response of the server request
     */
    private JsonElement internal;

    public AuthenticateAction getAction() {
        return action;
    }

    public void setAction(AuthenticateAction action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public String getFailoverReason() {
        return failoverReason;
    }

    public void setFailoverReason(String failoverReason) {
        this.failoverReason = failoverReason;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setInternal(JsonElement internal) {
        this.internal = internal;
    }
}
