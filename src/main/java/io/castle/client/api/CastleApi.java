package io.castle.client.api;

import com.google.common.collect.ImmutableMap;
import io.castle.client.model.*;
import io.castle.client.model.generated.*;

import java.util.List;

/**
 * Client for the Castle REST API.
 */
public interface CastleApi {

    /**
     * Merges an additional context object with the context object associated with this {@code CastleApi} instance.
     * The merged object is stored on the returned client and is not attached to {@code risk}, {@code filter}, or {@code log} payloads.
     * When the additional context is null, then the stored context is an empty JSON object.
     *
     * @param additionalContext client defined model, takes null
     * @return an API reference with the merged context values
     */
    CastleApi mergeContext(Object additionalContext);

    /**
     * Returns a client that stores the do-not-track flag.
     * The flag is not read by {@code risk}, {@code filter}, {@code log}, or other API methods.
     *
     * @param doNotTrack boolean stored on the returned client
     * @return a {@code CastleApi} reference that stores the given flag
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
     * Sends a POST request to {@code /v1/risk}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse risk(ImmutableMap<Object, Object> payload);

    /**
     * Sends a POST request to {@code /v1/risk}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
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
     * Sends a POST request to {@code /v1/filter}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse filter(ImmutableMap<Object, Object> payload);

    /**
     * Sends a POST request to {@code /v1/filter}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
     *
     * @param payload Event parameters
     * @return
     */
    FilterAndRiskResponse filter(Filter payload);

    /**
     * Sends a POST request to {@code /v1/log}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse log(ImmutableMap<Object, Object> payload);

    /**
     * Sends a POST request to {@code /v1/log}.
     * The payload is sent as given. Set {@code context} on the payload for IP and headers.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse log(Log payload);
}
