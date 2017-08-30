package io.castle.client.model;

public class Verdict {

    private AuthenticateAction action;
    private String userId;

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
}
