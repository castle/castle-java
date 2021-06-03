package io.castle.client.internal.utils;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.RiskPolicyResult;

public class VerdictTransportModel {

    private AuthenticateAction action;
    private RiskPolicyResult riskPolicy;
    private String userId;
    private String deviceToken;
    private float risk;

    public AuthenticateAction getAction() {
        return action;
    }

    public void setAction(AuthenticateAction action) {
        this.action = action;
    }

    public RiskPolicyResult getRiskPolicy() {
        return riskPolicy;
    }

    public void setRiskPolicy(RiskPolicyResult riskPolicy) {
        this.riskPolicy = riskPolicy;
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

    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }

}
