package io.castle.client.internal.utils;

import com.google.gson.JsonElement;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.RiskPolicyResult;
import io.castle.client.model.Verdict;

public class VerdictBuilder {
    private AuthenticateAction action;
    private RiskPolicyResult riskPolicy;
    private String userId;
    private boolean failover;
    private String failoverReason;
    private String deviceToken;
    private JsonElement internal;
    private int risk;

    private VerdictBuilder() {
    }

    public static VerdictBuilder success() {
        return new VerdictBuilder()
                .withFailover(false);
    }

    public static VerdictBuilder failover(String failoverReason) {
        return new VerdictBuilder()
                .withFailover(true)
                .withFailoverReason(failoverReason);
    }

    public VerdictBuilder withAction(AuthenticateAction action) {
        this.action = action;
        return this;
    }

    public VerdictBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }


    public VerdictBuilder withRiskPolicy(RiskPolicyResult riskPolicy) {
        this.riskPolicy = riskPolicy;
        return this;
    }

    public VerdictBuilder withDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public VerdictBuilder withRisk(final int risk) {
        this.risk = risk;
        return this;
    }

    public Verdict build() {
        Verdict verdict = new Verdict();
        verdict.setAction(action);
        verdict.setUserId(userId);
        verdict.setFailover(failover);
        verdict.setFailoverReason(failoverReason);
        verdict.setDeviceToken(deviceToken);
        verdict.setRiskPolicy(riskPolicy);
        verdict.setInternal(internal);
        verdict.setRisk(risk);
        return verdict;
    }

    public VerdictBuilder withFailover(boolean failover) {
        this.failover = failover;
        return this;
    }

    public VerdictBuilder withFailoverReason(String failoverReason) {
        this.failoverReason = failoverReason;
        return this;
    }

    public VerdictBuilder withInternal(JsonElement internal) {
        this.internal = internal;
        return this;
    }

    public static Verdict fromTransport(VerdictTransportModel transport, JsonElement internal) {
        internal.getAsJsonObject().get("action").getAsString();
        return success()
                .withAction(transport.getAction())
                .withUserId(transport.getUserId())
                .withDeviceToken(transport.getDeviceToken())
                .withRiskPolicy(transport.getRiskPolicy())
                .withRisk(transport.getRisk())
                .withInternal(internal)
                .build();
    }
}
