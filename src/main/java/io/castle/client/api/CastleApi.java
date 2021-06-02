package io.castle.client.api;

import com.google.gson.JsonElement;
import io.castle.client.model.*;

import javax.annotation.Nullable;

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
     * Makes a sync POST request to the authenticate endpoint containing all required parameters.
     * <p>
     * Optional parameters (that is, traits and properties) are set to null.
     *
     * @param event  a String representing an event understood by the Castle API
     * @param userId a String representing a user ID associated to an authentication attempt
     * @return a verdict that might result from a successful call to the Castle API or from the client's
     * {@link io.castle.client.model.AuthenticateFailoverStrategy}, in case of a failed call
     * @see <a href="https://api.castle.io/docs#authenticate">The docs</a>
     */
    Verdict authenticate(String event, String userId);

    /**
     * Makes a sync POST request to the authenticate endpoint containing required and optional parameters.
     *
     * @param event      a String representing an event understood by the Castle API
     * @param userId     a String representing a user ID associated to an authentication attempt
     * @param properties object for recording additional information connected to the event, takes null
     * @param traits     object for recording additional information connected to the user, takes null
     * @return a verdict that might result from a successful call to the Castle API or from the client's
     * {@link io.castle.client.model.AuthenticateFailoverStrategy}, in case of a failed call
     * @see <a href="https://api.castle.io/docs#authenticate">The docs</a>
     */
    Verdict authenticate(String event, String userId, @Nullable Object properties, @Nullable Object traits);

    /**
     * Makes a sync POST request to the authenticate endpoint containing required and optional parameters.
     * @param message Event parameters
     * @return a verdict that might result from a successful call to the Castle API or from the client's
     */
    Verdict authenticate(CastleMessage message);

    JsonElement buildAuthenticateRequest(CastleMessage request);

    Verdict sendAuthenticateRequest(JsonElement request);

    void sendAuthenticateRequest(JsonElement request, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Makes an async POST request to the authenticate endpoint containing required and optional parameters.
     *
     * @param event                a String representing an event understood by the Castle API
     * @param userId               a String representing a user ID associated to an authentication attempt
     * @param properties           object for recording additional information connected to the event, takes null
     * @param traits               object for recording additional information connected to the user, takes null
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     * @see <a href="https://api.castle.io/docs#authenticate">The docs</a>
     */
    void authenticateAsync(String event, String userId, @Nullable Object properties, @Nullable Object traits, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Makes an async POST request to the authenticate endpoint containing all required parameters.
     * <p>
     * Optional parameters (that is, traits and properties) are set to null.
     *
     * @param event                a String representing an event understood by the Castle API
     * @param userId               a String representing a user ID associated to an authentication attempt
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     * @see <a href="https://api.castle.io/docs#authenticate">The docs</a>
     */
    void authenticateAsync(String event, String userId, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Makes an async POST request to the authenticate endpoint containing required and optional parameters.
     * @param message Event parameters
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     */
    void authenticateAsync(CastleMessage message, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Sets the doNotTrack boolean of a new instance of {@code CastleApi}
     *
     * @param doNotTrack boolean representing the value that the doNotTrack private field of the new instance of
     *                   {@code CastleApi}
     * @return a {@code castleApi} reference whose doNotTrack private field is set to the doNotTrack parameter
     */
    CastleApi doNotTrack(boolean doNotTrack);

    /**
     * Makes an async POST request to the track endpoint containing all required parameters.
     *
     * @param event a String representing an event understood by the Castle API
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event);

    /**
     * Makes an async POST request to the track endpoint containing all required parameters and a user ID.
     *
     * @param event    a String representing an event understood by the Castle API
     * @param userId   a String representing a user ID
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event, @Nullable String userId);

    /**
     * Makes an async POST request to the track endpoint containing all required parameters and a user ID.
     *
     * @param event    a String representing an event understood by the Castle API
     * @param userId   a String representing a user ID
     * @param reviewId a String representing a reference to review ID
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event, @Nullable String userId, @Nullable String reviewId);

    /**
     * Makes an async POST request to the track endpoint containing required and optional parameters.
     *
     * @param event      a String representing an event understood by the Castle API
     * @param userId     a String representing a user ID
     * @param reviewId   a String representing a reference to review ID
     * @param properties object for recording additional information connected to the event, takes null
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties);

    /**
     * Makes an async POST request to the track endpoint containing required and optional parameters.
     *
     * @param event      a String representing an event understood by the Castle API
     * @param userId     a String representing a user ID
     * @param reviewId   a String representing a reference to review ID
     * @param properties object for recording additional information connected to the event, takes null
     * @param traits      object for recording additional information about the user like email or name, takes null
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object traits);

    /**
     * Makes an async POST request to the track endpoint containing required and optional parameters and a custom handler
     * for the async call's success and failure cases.
     *
     * @param event      a String representing an event understood by the Castle API
     * @param userId     a String representing a user ID
     * @param reviewId   a String representing a reference to review ID
     * @param properties object for recording additional information connected to the event, takes null
     * @param traits      object for recording additional information about the user like email or name, takes null
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     * @see <a href="https://api.castle.io/docs#track">The docs</a>
     */
    void track(String event, @Nullable String userId, @Nullable String reviewId, @Nullable Object properties, @Nullable Object traits, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     * Makes an async POST request to the track endpoint containing required and optional parameters.
     * @param message Event parameters
     */
    void track(CastleMessage message);

    JsonElement buildTrackRequest(CastleMessage request);

    void sendTrackRequest(JsonElement request);

    void sendTrackRequest(JsonElement request, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     * Makes an async POST request to the track endpoint containing required and optional parameters.
     * @param message Event parameters
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     */
    void track(CastleMessage message, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     * Makes a DELETE request to the privacy endpoint.
     *
     * @param userId             String representing a user id
     * @see <a href="https://castle.io/docs/gdpr_apis#user-data-purge-requests">The docs</a>
     */
    Boolean removeUser(String userId);

    /**
     * Makes a sync POST request to the approve device endpoint.
     *
     * @param deviceToken string representing the device to approve
     * @return device model object
     */
    CastleUserDevice approve(String deviceToken);

    /**
     * Makes a sync POST request to the report device endpoint.
     *
     * @param deviceToken string representing the device to report
     * @return device model object
     */
    CastleUserDevice report(String deviceToken);

    /**
     * Makes a sync GET request to the user devices endpoint.
     *
     * @param userId user unique ID
     * @return devices model object
     */
    CastleUserDevices userDevices(String userId);

    /**
     * Makes a sync GET request to the device endpoint.
     *
     * @param deviceToken string representing the device to report
     * @return device model object
     */
    CastleUserDevice device(String deviceToken);

    /**
     * Makes a sync POST request to the impersonate endpoint.
     *
     * @param userId user unique ID
     * @return
     */
    CastleSuccess impersonateStart(String userId);

    /**
     * Makes a sync POST request to the impersonate endpoint.
     *
     * @param userId user unique ID
     * @param impersonator description of impersonator, e.g., email
     * @return
     */
    CastleSuccess impersonateStart(String userId, String impersonator);

    /**
     * Makes a sync DELETE request to the impersonate endpoint.
     *
     * @param userId user unique ID
     * @return
     */
    CastleSuccess impersonateEnd(String userId);

    /**
     * Makes a sync DELETE request to the impersonate endpoint.
     *
     * @param userId user unique ID
     * @param impersonator description of impersonator, e.g., email
     * @return
     */
    CastleSuccess impersonateEnd(String userId, String impersonator);
}
