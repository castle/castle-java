package io.castle.client.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.internal.utils.Timestamp;
import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.model.*;
import io.castle.client.model.generated.*;
import javax.servlet.http.HttpServletRequest;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;

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
        JsonElement request = buildAuthenticateRequest(message);
        return sendAuthenticateRequest(request);
    }

    @Override
    public JsonElement buildAuthenticateRequest(CastleMessage message) {
        return buildJson(message);
    }

    @Override
    public Verdict sendAuthenticateRequest(JsonElement request) {
        Preconditions.checkNotNull(request, "Request json can not be null");

        if (doNotTrack) {
            return buildVerdictForDoNotTrack(request.getAsJsonObject().get("user_id").getAsString());
        }

        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendAuthenticateSync(request);
    }

    @Override
    public void sendAuthenticateRequest(JsonElement request, AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        Preconditions.checkNotNull(request, "Request json can not be null");

        if (doNotTrack) {
            asyncCallbackHandler.onResponse(buildVerdictForDoNotTrack(request.getAsJsonObject().get("user_id").getAsString()));
        } else {
            Preconditions.checkNotNull(asyncCallbackHandler, "The async handler can not be null");
            RestApi restApi = configuration.getRestApiFactory().buildBackend();
            restApi.sendAuthenticateAsync(request, asyncCallbackHandler);
        }
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
        JsonElement request = buildAuthenticateRequest(message);
        sendAuthenticateRequest(request, asyncCallbackHandler);
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
    public JsonElement buildTrackRequest(CastleMessage message) {
        Preconditions.checkNotNull(message.getEvent());
        return buildJson(message);
    }

    @Override
    public void sendTrackRequest(JsonElement request) {
        sendTrackRequest(request, null);
    }

    @Override
    public void sendTrackRequest(JsonElement request, AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        Preconditions.checkNotNull(request, "Request json can not be null");

        if (doNotTrack) {
            if (asyncCallbackHandler != null) {
                asyncCallbackHandler.onResponse(true);
            }
            return;
        }

        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        restApi.sendTrackRequest(request, asyncCallbackHandler);
    }

    @Override
    public void track(CastleMessage message, @Nullable AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        JsonElement messageJson = buildTrackRequest(message);

        sendTrackRequest(messageJson, asyncCallbackHandler);
    }

    @Override
    public Boolean removeUser(String userId) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendPrivacyRemoveUser(userId);
    }

    @Override
    public CastleUserDevice approve(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendApproveDeviceRequestSync(deviceToken);
    }

    @Override
    public CastleUserDevice report(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendReportDeviceRequestSync(deviceToken);
    }

    @Override
    public CastleUserDevices userDevices(String userId) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendGetUserDevicesRequestSync(userId);
    }

    @Override
    public CastleUserDevice device(String deviceToken) {
        Preconditions.checkNotNull(deviceToken);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendGetUserDeviceRequestSync(deviceToken);
    }

    @Override
    public CastleSuccess impersonateStart(String userId) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendImpersonateStartRequestSync(userId, null, contextJson);
    }

    @Override
    public CastleSuccess impersonateStart(String userId, String impersonator) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendImpersonateStartRequestSync(userId, impersonator, contextJson);
    }

    @Override
    public CastleSuccess impersonateEnd(String userId) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendImpersonateEndRequestSync(userId, "", contextJson);
    }

    @Override
    public CastleSuccess impersonateEnd(String userId, String impersonator) {
        Preconditions.checkNotNull(userId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.sendImpersonateEndRequestSync(userId, impersonator, contextJson);
    }

    @Override
    public CastleResponse get(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.get(path);
    }

    @Override
    public CastleResponse post(String path, Object payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(path, payload);
    }

    @Override
    public CastleResponse put(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(path);
    }

    @Override
    public CastleResponse put(String path, Object payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(path, payload);
    }

    @Override
    public CastleResponse delete(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(path);
    }

    @Override
    public CastleResponse delete(String path, Object payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(path, payload);
    }

    public CastleResponse risk(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_RISK, payload);
    }

    @Override
    public FilterAndRiskResponse risk(Risk payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(Castle.URL_RISK, payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), FilterAndRiskResponse.class);
    }

    @Override
    public ListResponse createList(ListRequest payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(Castle.URL_LISTS, payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListResponse.class);
    }

    @Override
    public CastleResponse deleteList(String id) {
        Preconditions.checkNotNull(id);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(String.format(Castle.URL_LISTS_ID, id));
    }

    @Override
    public List<ListResponse> searchLists(ListQuery payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(Castle.URL_LISTS_SEARCH, payload);
        Type listType = new TypeToken<List<ListResponse>>(){}.getType();
        return configuration.getModel().getGson().fromJson(castleResponse.json(), listType);
    }

    @Override
    public List<ListResponse> listAllLists() {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.get(Castle.URL_LISTS);
        Type listType = new TypeToken<List<ListResponse>>(){}.getType();
        return configuration.getModel().getGson().fromJson(castleResponse.json(), listType);
    }

    @Override
    public ListResponse updateList(String id, ListRequest payload) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.put(String.format(Castle.URL_LISTS_ID, id), payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListResponse.class);
    }

    @Override
    public ListResponse list(String id) {
        Preconditions.checkNotNull(id);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.get(String.format(Castle.URL_LISTS_ID, id));
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListResponse.class);
    }

    @Override
    public ListItem createListItem(String id, ListItemRequest payload) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(String.format(Castle.URL_LISTS_ITEMS, id), payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListItem.class);
    }

    @Override
    public ListItemsBatchResponse createOrUpdateListItems(String id, ListItemsBatchRequest payload) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(String.format(Castle.URL_LISTS_ITEMS_BATCH, id), payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListItemsBatchResponse.class);
    }

    @Override
    public List<ListItem> searchListItems(String id, ListItemQuery payload) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(String.format(Castle.URL_LISTS_ITEMS_SEARCH, id), payload);
        Type listType = new TypeToken<List<ListItem>>(){}.getType();
        return configuration.getModel().getGson().fromJson(castleResponse.json(), listType);
    }

    @Override
    public ListItemListCount countListItems(String id, ListItemQuery payload) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(String.format(Castle.URL_LISTS_ITEMS_COUNT, id), payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListItemListCount.class);
    }

    @Override
    public ListItem updateListItem(String listId, String itemId, ListItemRequest payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(String.format(Castle.URL_LISTS_ITEMS_UPDATE, listId, itemId), payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListItem.class);
    }

    @Override
    public ListItem getListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.get(String.format(Castle.URL_LISTS_ITEMS_GET, listId, itemId));
        return configuration.getModel().getGson().fromJson(castleResponse.json(), ListItem.class);
    }

    @Override
    public CastleResponse archiveListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(String.format(Castle.URL_LISTS_ITEMS_ARCHIVE, listId, itemId));
    }

    @Override
    public CastleResponse unarchiveListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(String.format(Castle.URL_LISTS_ITEMS_UNARCHIVE, listId, itemId));
    }

    @Override
    public CastleResponse filter(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_FILTER, payload);
    }

    @Override
    public FilterAndRiskResponse filter(Filter payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        CastleResponse castleResponse = restApi.post(Castle.URL_FILTER, payload);
        return configuration.getModel().getGson().fromJson(castleResponse.json(), FilterAndRiskResponse.class);
    }

    @Override
    public CastleResponse log(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_LOG, payload);
    }

    @Override
    public CastleResponse log(Log payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_LOG, payload);
    }

    @Override
    public CastleResponse recover(String userId) {
        Preconditions.checkNotNull(userId, "UserId can not be null");
        Preconditions.checkArgument(!userId.isEmpty());

        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(String.format(Castle.URL_RECOVER, userId));
    }

    private CastleMessage buildMessage(String event, String userId, @Nullable Object properties, @Nullable Object traits) {
        CastleMessage message = new CastleMessage(event);

        message.setUserId(userId);

        return setTraitsAndProperties(message, properties, traits);
    }

    private CastleMessage setTraitsAndProperties(CastleMessage message, @Nullable Object properties, @Nullable Object traits) {
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
