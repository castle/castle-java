package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Dump implementation of the castle backend. No calls to the Castle backend are realized.
 * <p>
 * This provider can be used in test environments.
 */
public class DumbRestApiBackend implements RestApi {

    private final CastleConfiguration configuration;

    public String event;
    public String userId;
    public JsonElement contextPayload;
    public JsonElement propertiesPayload;
    public JsonElement traits;
    public boolean active;


    public DumbRestApiBackend(CastleConfiguration configuration) {
        this.configuration = configuration;
    }

    private String jsonElementToString(JsonElement element) {
        String elementString = null;
        if (element != null) {
            elementString = element.toString();
        }
        return elementString;
    }

    // list of parameters added to dumb backend in order to test that events get passed correctly.
    public List<String> getListOfParameters() {

        ArrayList arrayList = new ArrayList();
        arrayList.add(this.event);
        arrayList.add(this.userId);
        arrayList.add(jsonElementToString(this.propertiesPayload));
        arrayList.add(jsonElementToString(this.contextPayload));
        arrayList.add(jsonElementToString(this.traits));
        arrayList.add(String.valueOf(active));

        return arrayList;
    }

    @Override
    public void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        this.event = event;
        this.userId = userId;
        this.contextPayload = contextPayload;
        this.propertiesPayload = propertiesPayload;
    }

    @Override
    public void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        // No-op
        if (asyncCallbackHandler != null) {
            asyncCallbackHandler.onResponse(true);
        }
    }

    @Override
    public AuthenticateAction sendAuthenticateSync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        return configuration.getFailoverStrategy().getDefaultAction();
    }

    @Override
    public void sendAuthenticateAsync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler) {
        if (asyncCallbackHandler != null) {
            asyncCallbackHandler.onResponse(configuration.getFailoverStrategy().getDefaultAction());
        }
    }

    @Override
    public void sendIdentifyRequest(String userId, JsonObject contextJson, boolean active, JsonElement traitsJson, JsonElement propertiesPayload) {
        this.userId = userId;
        this.contextPayload = contextJson;
        this.active = active;
        this.traits = traitsJson;
        this.propertiesPayload = propertiesPayload;
    }
}
