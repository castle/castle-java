package io.castle.client.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.model.*;

import javax.servlet.http.HttpServletRequest;

public class CastleApiImpl implements CastleApi {

    private final CastleSdkInternalConfiguration configuration;
    private final JsonObject contextJson;

    public CastleApiImpl(HttpServletRequest request, CastleSdkInternalConfiguration configuration) {
        this.configuration = configuration;
        CastleContext castleContext = buildContext(request);
        this.contextJson = configuration.getModel().getGson().toJsonTree(castleContext).getAsJsonObject();
    }

    public CastleApiImpl(CastleSdkInternalConfiguration configuration) {
        this.configuration = configuration;
        this.contextJson = null;
    }

    private CastleApiImpl(CastleSdkInternalConfiguration configuration, JsonObject contextJson) {
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
        return new CastleApiImpl(configuration, mergedContext);
    }

    @Override
    public CastleResponse get(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.get(path);
    }

    @Override
    public CastleResponse post(String path, ImmutableMap<Object, Object> payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(path, payload);
    }

    @Override
    public CastleResponse put(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(path);
    }

    @Override
    public CastleResponse put(String path, ImmutableMap<Object, Object> payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.put(path, payload);
    }

    @Override
    public CastleResponse delete(String path) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(path);
    }

    @Override
    public CastleResponse delete(String path, ImmutableMap<Object, Object> payload) {
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.delete(path, payload);
    }

    public CastleResponse risk(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_RISK, payload);
    }

    @Override
    public CastleResponse filter(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_FILTER, payload);
    }

    @Override
    public CastleResponse log(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        RestApi restApi = configuration.getRestApiFactory().buildBackend();
        return restApi.post(Castle.URL_LOG, payload);
    }

    @Override
    public CastleResponse createList(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().post(Castle.URL_LISTS, payload);
    }

    @Override
    public CastleResponse getAllLists() {
        return backend().get(Castle.URL_LISTS);
    }

    @Override
    public CastleResponse getList(String listId) {
        Preconditions.checkNotNull(listId);
        return backend().get(Castle.URL_LISTS + "/" + listId);
    }

    @Override
    public CastleResponse queryLists(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().post(Castle.URL_LISTS + "/query", payload);
    }

    @Override
    public CastleResponse updateList(String listId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(payload);
        return backend().put(Castle.URL_LISTS + "/" + listId, payload);
    }

    @Override
    public CastleResponse deleteList(String listId) {
        Preconditions.checkNotNull(listId);
        return backend().delete(Castle.URL_LISTS + "/" + listId);
    }

    @Override
    public CastleResponse createListItem(String listId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(payload);
        return backend().post(listItemsPath(listId), payload);
    }

    @Override
    public CastleResponse createListItemsBatch(String listId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(payload);
        return backend().post(listItemsPath(listId) + "/batch", payload);
    }

    @Override
    public CastleResponse getListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        return backend().get(listItemsPath(listId) + "/" + itemId);
    }

    @Override
    public CastleResponse queryListItems(String listId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(payload);
        return backend().post(listItemsPath(listId) + "/query", payload);
    }

    @Override
    public CastleResponse countListItems(String listId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(payload);
        return backend().post(listItemsPath(listId) + "/count", payload);
    }

    @Override
    public CastleResponse updateListItem(String listId, String itemId, ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        Preconditions.checkNotNull(payload);
        return backend().put(listItemsPath(listId) + "/" + itemId, payload);
    }

    @Override
    public CastleResponse archiveListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        return backend().delete(listItemsPath(listId) + "/" + itemId + "/archive");
    }

    @Override
    public CastleResponse unarchiveListItem(String listId, String itemId) {
        Preconditions.checkNotNull(listId);
        Preconditions.checkNotNull(itemId);
        return backend().put(listItemsPath(listId) + "/" + itemId + "/unarchive");
    }

    @Override
    public CastleResponse requestUserData(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().post(Castle.URL_PRIVACY + "users", payload);
    }

    @Override
    public CastleResponse deleteUserData(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().delete(Castle.URL_PRIVACY + "users", payload);
    }

    @Override
    public CastleResponse eventsSchema() {
        return backend().get(Castle.URL_EVENTS + "/schema");
    }

    @Override
    public CastleResponse queryEvents(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().post(Castle.URL_EVENTS + "/query", payload);
    }

    @Override
    public CastleResponse groupEvents(ImmutableMap<Object, Object> payload) {
        Preconditions.checkNotNull(payload);
        return backend().post(Castle.URL_EVENTS + "/group", payload);
    }

    private RestApi backend() {
        return configuration.getRestApiFactory().buildBackend();
    }

    private String listItemsPath(String listId) {
        return Castle.URL_LISTS + "/" + listId + "/items";
    }
}
