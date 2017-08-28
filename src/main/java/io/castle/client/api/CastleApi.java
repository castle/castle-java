package io.castle.client.api;

import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;

import javax.annotation.Nullable;

/**
 * Contains methods for calling the Castle.io API and the settings needed to properly make such a request.
 * <p>
 * Methods of this interface can be used to track security events and implement a work flow for adaptive authentication.
 * <p>
 * TODO: describe shortly identify and review
 * <p>
 * A {@code castleApi} instance contains all necessary configurations to correctly call the Castle.io API.
 * In particular, it contains:
 * <ul>
 * <li>a configuration object containing;
 * <li>an HTTP layer for handling HTTP requests and responses;
 * <li>a context object with metadata on the request made by the user to the server containing this client.
 * </ul><p>
 * <p>
 * The context object could have one of the following origins:
 * <ul>
 * <li>the default setting for a context object;
 * <li>T
 * <li>A context object with metadata on the request made by the user to the server containing this client.
 * </ul><p>
 */
public interface CastleApi {

    /**
     * merges an additional context object with the context object associated with this {@code CastleApi} instance
     * <p>
     * Given an
     *
     * @param additionalContext client defined model
     * @return an API reference with the merged context values.
     */
    CastleApi mergeContext(Object additionalContext);

    /**
     * @param event  TODO!!!!!!!!!!
     * @param userId TODO!!!!!!!!!!
     * @return TODO!!!!!!!!!!
     */
    AuthenticateAction authenticate(String event, String userId);

    /**
     * @param event      TODO!!!!!!!!!!
     * @param userId     TODO!!!!!!!!!!
     * @param properties TODO!!!!!!!!!!
     * @return
     */
    AuthenticateAction authenticate(String event, String userId, @Nullable Object properties);

    void authenticateAsync(String event, String userId, @Nullable Object properties, AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler);

    void authenticateAsync(String event, String userId, AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler);


    /**
     * @param doNotTrack TODO!!!!!!!!!!
     * @return TODO!!!!!!!!!!
     */
    CastleApi doNotTrack(boolean doNotTrack);

    /**
     * @param event TODO!!!!!!!!!!
     */
    void track(String event);

    /**
     * @param event  TODO!!!!!!!!!!
     * @param userId TODO!!!!!!!!!!
     */
    void track(String event, @Nullable String userId);

    /**
     * @param event      TODO!!!!!!!!!!
     * @param userId     TODO!!!!!!!!!!
     * @param properties TODO!!!!!!!!!!
     */
    void track(String event, @Nullable String userId, @Nullable Object properties);

    /**
     * @param event      TODO!!!!!!!!!!
     * @param userId     TODO!!!!!!!!!!
     * @param properties TODO!!!!!!!!!!
     */
    void track(String event, @Nullable String userId, @Nullable Object properties, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     * Call to the identify endpoint @see <a href="https://api.castle.io/docs#identify">The docs</a>
     *
     * @param userId user unique ID
     * @param active is this call realized in an active user session
     */
    void identify(String userId, boolean active);

    /**
     * Call to the identify endpoint @see <a href="https://api.castle.io/docs#identify">The docs</a>
     *
     * @param userId     user unique ID
     * @param active     is this call realized in an active user session
     * @param traits     additional traits parameters to send
     * @param properties additional properties parameters to send
     */
    void identify(String userId, boolean active, @Nullable Object traits, @Nullable Object properties);

}
