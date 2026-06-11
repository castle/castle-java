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
import io.castle.client.model.*;
import io.castle.client.model.generated.*;
import jakarta.servlet.http.HttpServletRequest;

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
    public CastleResponse requestUserData(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_PRIVACY + "users", payload);
    }

    @Override
    public CastleResponse deleteUserData(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(Castle.URL_PRIVACY + "users", payload);
    }

    @Override
    public CastleResponse eventsSchema() {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.get(Castle.URL_EVENTS + "/schema");
    }

    @Override
    public CastleResponse queryEvents(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_EVENTS + "/query", payload);
    }

    @Override
    public CastleResponse groupEvents(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_EVENTS + "/group", payload);
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

}
