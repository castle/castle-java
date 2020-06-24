package io.castle.client.model;

import com.google.gson.JsonObject;

public class ImpersonatePayload {
    private final String userId;
    private final JsonObject context;
    private final JsonObject properties;

    public ImpersonatePayload(String userId, String impersonator, JsonObject contextJson) {
        this.userId = userId;
        this.properties = new JsonObject();
        this.properties.addProperty("impersonator", impersonator);
        this.context = contextJson;
    }

    public ImpersonatePayload(String userId, JsonObject contextJson) {
        this.userId = userId;
        this.properties = new JsonObject();
        this.context = contextJson;
    }
}
