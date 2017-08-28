package io.castle.client.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.CastleContext;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

public class CastleApiImpl implements CastleApi {

    private final HttpServletRequest request;
    private final boolean doNotTrack;
    private final CastleSdkInternalConfiguration configuration;
    private final JsonObject contextJson;

    public CastleApiImpl(HttpServletRequest request, boolean doNotTrack, CastleSdkInternalConfiguration configuration) {
        this.request = request;
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        CastleContext castleContext = buildContext();
        this.contextJson = configuration.getModel().getGson().toJsonTree(castleContext).getAsJsonObject();
    }

    private CastleApiImpl(HttpServletRequest request, boolean doNotTrack, CastleSdkInternalConfiguration configuration, JsonObject contextJson) {
        this.request = request;
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        this.contextJson = contextJson;
    }

    private CastleContext buildContext() {
        CastleContextBuilder builder = new CastleContextBuilder(configuration.getConfiguration());
        CastleContext context = builder
                .fromHttpServletRequest(this.request)
                .build();
        return context;
    }

    @Override
    public CastleApi mergeContext(Object additionalContext) {
        JsonObject contextToMerge = configuration.getModel().getGson().toJsonTree(additionalContext).getAsJsonObject();
        JsonObject mergedContext = new ContextMerge().merge(this.contextJson, contextToMerge);
        return new CastleApiImpl(request, doNotTrack, configuration, mergedContext);
    }

    @Override
    public CastleApi doNotTrack(boolean doNotTrack) {
        return new CastleApiImpl(request, doNotTrack, configuration);
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId) {
        return authenticate(event, userId, null);
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId, Object properties) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        return restApi.sendAuthenticateSync(event, userId, contextJson, propertiesJson);
    }

    @Override
    public void authenticateAsync(String event, String userId, @Nullable Object properties, AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        restApi.sendAuthenticateAsync(event, userId, contextJson, propertiesJson, asyncCallbackHandler);

    }

    @Override
    public void authenticateAsync(String event, String userId, AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler) {
        authenticateAsync(event, userId, null, asyncCallbackHandler);
    }

    @Override
    public void track(String event) {
        track(event, null, null, null);
    }

    @Override
    public void track(String event, String userId) {
        track(event, userId, null, null);
    }

    @Override
    public void track(String event, String userId, Object properties) {
        track(event, userId, properties, null);
    }

    @Override
    public void track(String event, @Nullable String userId, @Nullable Object properties, AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        if (doNotTrack) {
            return;
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        restApi.sendTrackRequest(event, userId, contextJson, propertiesJson, asyncCallbackHandler);
    }

    @Override
    public void identify(String userId, boolean active, Object traits, Object properties) {
        if (doNotTrack) {
            return;
        }
        JsonElement traitsJson = null;
        if (traits != null) {
            traitsJson = configuration.getModel().getGson().toJsonTree(traits);
        }
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }

        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendIdentifyRequest(userId, contextJson, active, traitsJson, propertiesJson);
    }

    @Override
    public void identify(String userId, boolean active) {
        identify(userId, active, null, null);
    }

    //TODO Ask about the review endpoint. How to get the review ids??.
}
