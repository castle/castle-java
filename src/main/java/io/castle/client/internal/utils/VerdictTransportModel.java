package io.castle.client.internal.utils;

import io.castle.client.model.AuthenticateAction;

public class VerdictTransportModel {

    private AuthenticateAction action;
    private String userId;
    private String deviceToken;

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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
