package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;

/**
 * Dump implementation of the castle backend. No calls to the Castle backend are realized.
 * <p>
 * This provider can be used in test environments.
 */
public class DumpRestApiBackend implements RestApi {

    private final CastleConfiguration configuration;

    public DumpRestApiBackend(CastleConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        // No-op
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
        //no-op
    }
}
