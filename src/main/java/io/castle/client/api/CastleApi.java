package io.castle.client.api;

import com.google.common.collect.ImmutableMap;
import io.castle.client.model.*;

/**
 * Contains methods for calling the Castle API and the settings needed to properly make such a request.
 * <p>
 * A {@code castleApi} contains all necessary configurations to correctly call the Castle API.
 * In particular, it contains:
 * <ul>
 * <li>a configuration object;
 * <li>an HTTP layer for handling HTTP requests and responses;
 * <li>a context object with metadata on the request made by the user to the server containing this client.
 * </ul>
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

    CastleResponse get(String path);

    CastleResponse post(String path, ImmutableMap<Object, Object> payload);

    CastleResponse put(String path);

    CastleResponse put(String path, ImmutableMap<Object, Object> payload);

    CastleResponse delete(String path);

    CastleResponse delete(String path, ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the risk endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse risk(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the filter endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse filter(ImmutableMap<Object, Object> payload);

    /**
     * Makes a sync POST request to the log endpoint.
     *
     * @param payload Event parameters
     * @return
     */
    CastleResponse log(ImmutableMap<Object, Object> payload);

    // Lists API

    /**
     * Creates a new list.
     *
     * @param payload list attributes
     * @return a decoded json response
     */
    CastleResponse createList(ImmutableMap<Object, Object> payload);

    /**
     * Returns all lists.
     *
     * @return a decoded json response
     */
    CastleResponse getAllLists();

    /**
     * Returns a single list.
     *
     * @param listId list id
     * @return a decoded json response
     */
    CastleResponse getList(String listId);

    /**
     * Queries lists matching the given filters.
     *
     * @param payload query filters
     * @return a decoded json response
     */
    CastleResponse queryLists(ImmutableMap<Object, Object> payload);

    /**
     * Updates an existing list.
     *
     * @param listId  list id
     * @param payload list attributes to update
     * @return a decoded json response
     */
    CastleResponse updateList(String listId, ImmutableMap<Object, Object> payload);

    /**
     * Deletes a list.
     *
     * @param listId list id
     * @return a decoded json response
     */
    CastleResponse deleteList(String listId);

    // List items API

    /**
     * Creates a new item in a list.
     *
     * @param listId  list id
     * @param payload item attributes
     * @return a decoded json response
     */
    CastleResponse createListItem(String listId, ImmutableMap<Object, Object> payload);

    /**
     * Creates several items in a list in a single batch request.
     *
     * @param listId  list id
     * @param payload batch of item attributes
     * @return a decoded json response
     */
    CastleResponse createListItemsBatch(String listId, ImmutableMap<Object, Object> payload);

    /**
     * Returns a single item from a list.
     *
     * @param listId list id
     * @param itemId item id
     * @return a decoded json response
     */
    CastleResponse getListItem(String listId, String itemId);

    /**
     * Queries items of a list matching the given filters.
     *
     * @param listId  list id
     * @param payload query filters
     * @return a decoded json response
     */
    CastleResponse queryListItems(String listId, ImmutableMap<Object, Object> payload);

    /**
     * Counts items of a list matching the given filters.
     *
     * @param listId  list id
     * @param payload query filters
     * @return a decoded json response
     */
    CastleResponse countListItems(String listId, ImmutableMap<Object, Object> payload);

    /**
     * Updates an item of a list.
     *
     * @param listId  list id
     * @param itemId  item id
     * @param payload item attributes to update
     * @return a decoded json response
     */
    CastleResponse updateListItem(String listId, String itemId, ImmutableMap<Object, Object> payload);

    /**
     * Archives an item of a list.
     *
     * @param listId list id
     * @param itemId item id
     * @return a decoded json response
     */
    CastleResponse archiveListItem(String listId, String itemId);

    /**
     * Unarchives an item of a list.
     *
     * @param listId list id
     * @param itemId item id
     * @return a decoded json response
     */
    CastleResponse unarchiveListItem(String listId, String itemId);

    // Privacy API

    /**
     * Requests the data Castle holds for a user.
     *
     * @param payload privacy request parameters
     * @return a decoded json response
     */
    CastleResponse requestUserData(ImmutableMap<Object, Object> payload);

    /**
     * Requests deletion of the data Castle holds for a user.
     *
     * @param payload privacy request parameters
     * @return a decoded json response
     */
    CastleResponse deleteUserData(ImmutableMap<Object, Object> payload);

    // Events API

    /**
     * Returns the schema of available events.
     *
     * @return a decoded json response
     */
    CastleResponse eventsSchema();

    /**
     * Queries events matching the given filters.
     *
     * @param payload query filters
     * @return a decoded json response
     */
    CastleResponse queryEvents(ImmutableMap<Object, Object> payload);

    /**
     * Groups events matching the given filters.
     *
     * @param payload query filters
     * @return a decoded json response
     */
    CastleResponse groupEvents(ImmutableMap<Object, Object> payload);
}
