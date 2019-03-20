package io.castle.client.model;

import com.google.gson.JsonObject;

public class ImpersonatePayload {
    private final String userId;
    private final String impersonator;
    private final JsonObject context;

    public ImpersonatePayload(String userId, String impersonator, JsonObject contextJson) {
        this.userId = userId;
        this.impersonator = impersonator;
        this.context = contextJson;
    }

    public ImpersonatePayload(String userId, JsonObject contextJson) {
        this.userId = userId;
        this.impersonator = null;
        this.context = contextJson;
    }
}
