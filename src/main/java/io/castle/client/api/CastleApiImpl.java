package io.castle.client.api;

import com.google.gson.JsonElement;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.model.AuthenticateAction;
import io.castle.client.internal.model.CastleContext;

import javax.servlet.http.HttpServletRequest;

public class CastleApiImpl implements CastleApi {

    private final HttpServletRequest request;
    private final boolean doNotTrack;
    private final CastleSdkInternalConfiguration configuration;
    private final CastleContext context;

    public CastleApiImpl(HttpServletRequest request, boolean doNotTrack, CastleSdkInternalConfiguration configuration) {
        this.request = request;
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        this.context = buildContext();
    }

    private CastleContext buildContext() {
        //TODO create a full castle context object
        CastleContext creating = new CastleContext();
        creating.setIp("12.12.12.12");
        return creating;
    }

    @Override
    public CastleApi mergeContext(Object additionalContext) {
        return this;
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId) {
        return authenticate(event, userId, null);
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId, Object properties) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement contextJson = configuration.getModel().getGson().toJsonTree(context);
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        return restApi.sendAuthenticateSync(event, userId, contextJson, propertiesJson);
    }

    @Override
    public void track(String event) {
        track(event, null, null);
    }

    @Override
    public void track(String event, String userId) {
        track(event, userId, null);
    }

    @Override
    public void track(String event, String userId, Object properties) {
        if (doNotTrack) {
            return;
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement contextJson = configuration.getModel().getGson().toJsonTree(context);
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        restApi.sendTrackRequest(event, userId, contextJson, propertiesJson);
    }

    @Override
    public void identify(String userId, boolean active, Object traits) {
        if (doNotTrack) {
            return;
        }
    }
}
