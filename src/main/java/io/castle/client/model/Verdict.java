package io.castle.client.model;

public class Verdict {

    private AuthenticateAction action;
    private String userId;
    private boolean failover;
    private String failoverReason;

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
}
