package io.castle.client.internal;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.internal.utils.Timestamp;
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

    public CastleApiImpl(CastleSdkInternalConfiguration configuration, boolean doNotTrack) {
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        this.contextJson = null;
    }

    private CastleApiImpl(boolean doNotTrack, CastleSdkInternalConfiguration configuration, JsonObject contextJson) {
        this.doNotTrack = doNotTrack;
        this.configuration = configuration;
        this.contextJson = contextJson;
    }

    private CastleContext buildContext(HttpServletRequest request) {
        CastleContextBuilder builder = new CastleContextBuilder(configuration.getConfiguration(), configuration.getModel());
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
        return authenticate(buildMessage(event, userId, properties, traits));
    }

    @Override
    public Verdict authenticate(CastleMessage message) {
        if (doNotTrack) {
            return buildVerdictForDoNotTrack(message.getUserId());
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement messageJson = buildJson(message);
        return restApi.sendAuthenticateSync(messageJson);
    }

    private Verdict buildVerdictForDoNotTrack(String userId) {
        return VerdictBuilder.failover("Castle set to do not track.")
                .withAction(AuthenticateAction.ALLOW)
                .withUserId(userId)
                .build();
    }

    @Override
    public void authenticateAsync(String event, String userId, @Nullable Object properties, @Nullable Object traits, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        authenticateAsync(
            buildMessage(event, userId, properties, traits),
            asyncCallbackHandler
        );
    }

    @Override
    public void authenticateAsync(String event, String userId, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        authenticateAsync(
            CastleMessage.builder(event).userId(userId).build(),
            asyncCallbackHandler
        );
    }

    @Override
    public void authenticateAsync(CastleMessage message, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        if (doNotTrack) {
            asyncCallbackHandler.onResponse(buildVerdictForDoNotTrack(message.getUserId()));
        } else {
            Preconditions.checkNotNull(asyncCallbackHandler, "The async handler can not be null");
            RestApi restApi = configuration.getRestApiFactory().buildBackend();
            JsonElement messageJson = buildJson(message);
            restApi.sendAuthenticateAsync(messageJson, asyncCallbackHandler);
        }
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
    public void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object traits) {
        track(event, userId, reviewId, properties, traits, null);
    }

    @Override
    public void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object traits, AsyncCallbackHandler<Boolean> asyncCallbackHandler) {

        CastleMessage message = buildMessage(event, userId, properties, traits);

        if (reviewId != null) {
            message.setReviewId(reviewId);
        }

        track(message, asyncCallbackHandler);
    }

    @Override
    public void track(CastleMessage message) {
        track(message, null);
    }

    @Override
    public void track(CastleMessage message, @Nullable AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        Preconditions.checkNotNull(message.getEvent());
        if (doNotTrack) {
            if (asyncCallbackHandler != null) {
                asyncCallbackHandler.onResponse(true);
            }
            return;
        }
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        JsonElement messageJson = buildJson(message);
        restApi.sendTrackRequest(messageJson, asyncCallbackHandler);
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

    @Override
    public CastleUserDevice approve(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendApproveDeviceRequestSync(deviceToken);
    }

    @Override
    public void approveAsync(String deviceToken, AsyncCallbackHandler<CastleUserDevice> asyncCallbackHandler) {
        Preconditions.checkNotNull(deviceToken);
        Preconditions.checkNotNull(asyncCallbackHandler);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendApproveDeviceRequestAsync(deviceToken, asyncCallbackHandler);
    }

    @Override
    public CastleUserDevice report(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendReportDeviceRequestSync(deviceToken);
    }

    @Override
    public void reportAsync(String deviceToken, AsyncCallbackHandler<CastleUserDevice> asyncCallbackHandler) {
        Preconditions.checkNotNull(deviceToken);
        Preconditions.checkNotNull(asyncCallbackHandler);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendReportDeviceRequestAsync(deviceToken, asyncCallbackHandler);
    }

    @Override
    public CastleUserDevices userDevices(String userId) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendGetUserDevicesRequestSync(userId);
    }

    @Override
    public void userDevicesAsync(String userId, AsyncCallbackHandler<CastleUserDevices> asyncCallbackHandler) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(asyncCallbackHandler);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendGetUserDevicesRequestAsync(userId, asyncCallbackHandler);
    }

    @Override
    public CastleUserDevice device(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendGetUserDeviceRequestSync(deviceToken);
    }

    @Override
    public void deviceAsync(String deviceToken, AsyncCallbackHandler<CastleUserDevice> asyncCallbackHandler) {
        Preconditions.checkNotNull(deviceToken);
        Preconditions.checkNotNull(asyncCallbackHandler);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendGetUserDeviceRequestAsync(deviceToken, asyncCallbackHandler);
    }

    private CastleMessage buildMessage(String event, String userId, @Nullable Object properties, @Nullable Object traits) {
        CastleMessage message = new CastleMessage(event);

        message.setUserId(userId);

        if (properties != null) {
            JsonElement propertiesJson = configuration.getModel().getGson().toJsonTree(properties);
            message.setProperties(propertiesJson);
        }

        if (traits != null) {
            JsonElement traitsJson = configuration.getModel().getGson().toJsonTree(traits);
            message.setUserTraits(traitsJson);
        }

        return message;
    }

    private JsonElement buildJson(CastleMessage message) throws CastleRuntimeException {
        JsonObject contextJson;
        // Context can be either from the message or from the instance of this
        // class. Make sure we have one
        CastleContext context = message.getContext();
        if (context == null) {
            contextJson = this.contextJson;
        } else {
            contextJson = configuration.getModel().getGson().toJsonTree(context).getAsJsonObject();
        }

        JsonElement messageJson = configuration.getModel().getGson().toJsonTree(message);
        JsonObject messageObj = messageJson.getAsJsonObject();
        messageObj.add("context", contextJson);

        // Add sent_at to json
        messageObj.addProperty("sent_at", Timestamp.timestamp());

        return messageObj;
    }
}
