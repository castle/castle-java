package io.castle.client.api;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import io.castle.client.model.*;
import io.castle.client.model.generated.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Contains methods for calling the Castle API and the settings needed to properly make such a request.
 * <p>
 * Methods of this interface can be used to make calls to Castle's API
 * {@code /v1/authenticate} and {@code /v1/track} endpoints.
 * <p>
 * A {@code castleApi} contains all necessary configurations to correctly call the Castle API.
 * In particular, it contains:
 * <ul>
 * <li>a configuration object containing;
 * <li>an HTTP layer for handling HTTP requests and responses;
 * <li>a context object with metadata on the request made by the user to the server containing this client.
 * </ul><p>
 * The context object could have one of the following origins:
 * <ul>
 * <li>the default setting for a context object;
 * <li>T
 * <li>A context object with metadata on the request made by the user to the server containing this client.
 * </ul><p>
 * An instance of {@code CastleApi} contains a boolean named doNotTrack in a private field.
 * When doNotTrack is set to false, the CastleApi instance created is configured to make requests when any
 * of its methods is called.
 * When set to true, authenticate and track methods return immediately without making any request.
 * The {@code this#authenticate} method will resort to the {@link io.castle.client.model.AuthenticateFailoverStrategy}
 * with authenticate action set to {@link io.castle.client.model.AuthenticateAction#ALLOW}.
 * When doNotTrack is set to true, this will resort to the {@link io.castle.client.model.AuthenticateFailoverStrategy}
 * with authenticate action set to {@link io.castle.client.model.AuthenticateAction#ALLOW}.
 */
public interface CastleApi {

    /**
     * Merges an additional context object with the context object associated with this {@code CastleApi} instance.
     * <p>
     * When the additional context is null, then the returned context is an empty JSON object.
     *
     * @param additionalContext client defined model, takes null
     * @return an API reference with the merged context values
     */
    CastleApi mergeContext(Object additionalContext);

    /**
     * Sets the doNotTrack boolean of a new instance of {@code CastleApi}
     *
     * @param doNotTrack boolean representing the value that the doNotTrack private field of the new instance of
     *                   {@code CastleApi}
     * @return a {@code castleApi} reference whose doNotTrack private field is set to the doNotTrack parameter
     */
    CastleApi doNotTrack(boolean doNotTrack);

    CastleResponse get(String path);

    CastleResponse post(String path, Object payload);

    CastleResponse put(String path);

    CastleResponse put(String path, Object payload);

    CastleResponse delete(String path);

    CastleResponse delete(String path, Object payload);

    /**
     * Makes a sync POST request to the privacy endpoint to request a user's data.
     *
     * @param payload request parameters
     * @return a decoded json response
     */
    CastleResponse requestUserData(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync DELETE request to the privacy endpoint to delete a user's data.
     *
     * @param payload request parameters
     * @return a decoded json response
     */
    CastleResponse deleteUserData(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync GET request to the events schema endpoint.
     *
     * @return a decoded json response
     */
    CastleResponse eventsSchema();

    /**
     * Makes a sync POST request to the events query endpoint.
     *
     * @param payload query parameters
     * @return a decoded json response
     */
    CastleResponse queryEvents(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the events group endpoint.
     *
     * @param payload group parameters
     * @return a decoded json response
     */
    CastleResponse groupEvents(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the risk endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse risk(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the risk endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    FilterAndRiskResponse risk(Risk payload);

    /**
     * Makes a sync POST request to the list endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    ListResponse createList(ListRequest payload);

    /**
     * Makes a sync GET request to the list endpoint.
     * @return
     */
    List<ListResponse> listAllLists();

    /**
     * Makes a sync GET request to the list endpoint.
     *
     * @param listId List ID
     * @return
     */
    ListResponse list(String listId);

    /**
     * Makes a sync PUT request to the list endpoint.
     *
     * @param listId List ID
     * @param payload Event parameters
     * @return
     */
    ListResponse updateList(String listId, ListRequest payload);

    /**
     * Makes a sync DELETE request to the list endpoint.
     *
     * @param listId List ID
     * @return
     */
    CastleResponse deleteList(String listId);

    /**
     * Makes a sync POST request to the list endpoint.
     *
     * @param payload Search parameters
     * @return
     */
    List<ListResponse> searchLists(ListQuery payload);

    /**
     * Makes a sync POST request to the list item endpoint.
     *
     * @param listId List ID
     * @param payload Event parameters
     * @return
     */
    ListItem createListItem(String listId, ListItemRequest payload);

    /**
     * Makes a sync POST request to the list item endpoint.
     *
     * @param id List ID
     * @param payload Event parameters
     * @return
     */
    ListItemsBatchResponse createOrUpdateListItems(String id, ListItemsBatchRequest payload);

    /**
     * Makes a sync GET request to the list item endpoint.
     *
     * @return
     */
    List<ListItem> searchListItems(String listId, ListItemQuery payload);

    /**
     * Makes a sync GET request to the list item endpoint.
     *
     * @param listId List ID
     * @param payload Event parameters
     * @return
     */
    ListItemListCount countListItems(String listId, ListItemQuery payload);

    /**
     * Makes a sync GET request to the list item endpoint.
     *
     * @param listId List ID
     * @param itemid Item ID
     * @return
     */
    ListItem updateListItem(String listId, String itemid, ListItemRequest payload);

    /**
     * Makes a sync GET request to the list item endpoint.
     *
     * @param listId List ID
     * @param itemid Item ID
     * @return
     */
    ListItem getListItem(String listId, String itemid);

    CastleResponse archiveListItem(String listId, String itemid);

    CastleResponse unarchiveListItem(String listId, String itemid);

    /**
     * Makes a sync POST request to the filter endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse filter(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the filter endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    FilterAndRiskResponse filter(Filter payload);

    /**
     * Makes a sync POST request to the log endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse log(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the log endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse log(Log payload);
}
