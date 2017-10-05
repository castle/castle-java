package io.castle.client.internal;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.model.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

public class CastleApiImpl implements CastleApi {

    private final boolean doNotTrack;
    private final CastleSdkInternalConfiguration configuration;
    private final JsonObject contextJson;

    public CastleApiImpl(HttpServletRequest request, boolean doNotTrack, CastleSdkInternalConfiguration configuration) {
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        CastleContext castleContext = buildContext(request);
        this.contextJson = configuration.getModel().getGson().toJsonTree(castleContext).getAsJsonObject();
    }

    private CastleApiImpl(boolean doNotTrack, CastleSdkInternalConfiguration configuration, JsonObject contextJson) {
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        this.contextJson = contextJson;
    }

    private CastleContext buildContext(HttpServletRequest request) {
        CastleContextBuilder builder = new CastleContextBuilder(configuration.getConfiguration());
        CastleContext context = builder
                .fromHttpServletRequest(request)
                .build();
        return context;
    }

    @Override
    public CastleApi mergeContext(Object additionalContext) {
        JsonObject contextToMerge = null;
        if (additionalContext != null) {
            contextToMerge = configuration.getModel().getGson().toJsonTree(additionalContext).getAsJsonObject();
        }
        JsonObject mergedContext = new ContextMerge().merge(this.contextJson, contextToMerge);
        return new CastleApiImpl(doNotTrack, configuration, mergedContext);
    }

    @Override
    public CastleApi doNotTrack(boolean doNotTrack) {
        return new CastleApiImpl(doNotTrack, configuration, contextJson);
    }

    @Override
    public Verdict authenticate(String event, String userId) {
        return authenticate(event, userId, null, null);
    }

    @Override
    public Verdict authenticate(String event, String userId, @Nullable Object properties, @Nullable Object traits) {
        if (doNotTrack) {
            return buildVerdictForDoNotTrack(userId);
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        JsonElement traitsJson = null;
        if (traits != null) {
            traitsJson = configuration.getModel().getGson().toJsonTree(traits);
        }
        return restApi.sendAuthenticateSync(event, userId, contextJson, propertiesJson, traitsJson);
    }

    private Verdict buildVerdictForDoNotTrack(String userId) {
        return VerdictBuilder.failover("Castle set to do not track.")
                .withAction(AuthenticateAction.ALLOW)
                .withUserId(userId)
                .build();
    }

    @Override
    public void authenticateAsync(String event, @Nullable String userId, @Nullable Object properties, @Nullable Object traits, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        if (doNotTrack) {
            asyncCallbackHandler.onResponse(buildVerdictForDoNotTrack(userId));
        } else {
            Preconditions.checkNotNull(asyncCallbackHandler, "The async handler can not be null");
            RestApi restApi = configuration.getRestApiFactory().buildBackend();
            JsonElement propertiesJson = null;
            if (properties != null) {
                propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
            }
            JsonElement traitsJson = null;
            if (traits != null) {
                traitsJson = configuration.getModel().getGson().toJsonTree(traits);
            }
            restApi.sendAuthenticateAsync(event, userId, contextJson, propertiesJson, traitsJson, asyncCallbackHandler);
        }
    }

    @Override
    public void authenticateAsync(String event, String userId, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        authenticateAsync(event, userId, null, null, asyncCallbackHandler);
    }

    @Override
    public void track(String event) {
        track(event, null, null, null, null);
    }

    @Override
    public void track(String event, String userId) {
        track(event, userId, null, null, null);
    }

    @Override
    public void track(String event, @Nullable String userId, @Nullable String reviewId) {
        track(event, userId, reviewId, null, null, null);
    }

    @Override
    public void track(String event, String userId, String reviewId, Object properties) {
        track(event, userId, reviewId, properties, null, null);
    }

    @Override
    public void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object trait) {
        track(event, userId, reviewId, properties, trait, new DefaultTrackAsyncCallbackHandler());
    }

    @Override
    public void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object trait, AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        Preconditions.checkNotNull(event);
        if (doNotTrack) {
            asyncCallbackHandler.onResponse(true);
            return;
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement propertiesJson = null;
        if (properties != null) {
            propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
        }
        JsonElement traitJson = null;
        if (trait != null) {
            traitJson = configuration.getModel().getGson().toJsonTree(trait);
        }
        restApi.sendTrackRequest(event, userId, reviewId, contextJson, propertiesJson, traitJson, asyncCallbackHandler);
    }

    @Override
    public void identify(String userId, @Nullable Object traits, boolean active) {
        Preconditions.checkNotNull(userId);
        if (doNotTrack) {
            return;
        }
        JsonElement traitsJson = null;
        if (traits != null) {
            traitsJson = configuration.getModel().getGson().toJsonTree(traits);
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendIdentifyRequest(userId, contextJson, active, traitsJson);
    }

    @Override
    public void identify(String userId) {
        identify(userId, null, true);
    }

    @Override
    public void identify(String userId, @Nullable Object traits) {
        Preconditions.checkNotNull(userId);
        identify(userId, traits, true);
    }


    @Override
    public Review review(String reviewId) {
        Preconditions.checkNotNull(reviewId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendReviewRequestSync(reviewId);
    }

    @Override
    public void reviewAsync(String reviewId, AsyncCallbackHandler<Review> asyncCallbackHandler) {
        Preconditions.checkNotNull(reviewId);
        Preconditions.checkNotNull(asyncCallbackHandler);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendReviewRequestAsync(reviewId, asyncCallbackHandler);
    }

}
