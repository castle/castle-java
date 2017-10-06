package io.castle.client.api;

import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.Review;
import io.castle.client.model.Verdict;

import javax.annotation.Nullable;

/**
 * Contains methods for calling the Castle API and the settings needed to properly make such a request.
 * <p>
 * Methods of this interface can be used to make calls to Castle's API
 * {@code /v1/identify}, {@code /v1/track}, {@code /v1/reviews/} and {@code /v1/identify} endpoints.
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
 * When set to true, authenticate, track and identify methods return immediately without making any request.
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
     * Makes an async POST request to the identify endpoint with all required parameters.
     * <p>
     * traits parameter is null by default.
     * active parameter is true by default.
     *
     * @param userId user unique ID
     * @see <a href="https://api.castle.io/docs#identify">The docs</a>
     */
    void identify(String userId);

    /**
     * Makes an async POST request to the identify endpoint with an additional traits object.
     * <p>
     * active parameter is true by default.
     *
     * @param userId user unique ID
     * @param traits object for recording additional information connected to the user, takes null
     * @see <a href="https://api.castle.io/docs#identify">The docs</a>
     */
    void identify(String userId, Object traits);

    /**
     * Makes an async POST request to the identify endpoint
     *
     * @param userId user unique ID
     * @param traits object for recording additional information connected to the user, takes null
     * @param active is this call associated to an active user session
     * @see <a href="https://api.castle.io/docs#identify">The docs</a>
     */
    void identify(String userId, @Nullable Object traits, boolean active);

    /**
     * Makes a sync GET request to the review endpoint.
     *
     * @param reviewId String representing a user id
     * @return review model object
     * @see <a href="https://api.castle.io/docs#review">The docs</a>
     */
    Review review(String reviewId);

    /**
     * Makes an async GET request to the review endpoint.
     *
     * @param reviewId             String representing a user id
     * @param asyncCallbackHandler a user-implemented instance of {@code AsyncCallbackHandler} which specifies
     *                             how to handle success of failure of authenticate API calls
     * @see <a href="https://api.castle.io/docs#review">The docs</a>
     */
    void reviewAsync(String reviewId, AsyncCallbackHandler<Review> asyncCallbackHandler);
}
