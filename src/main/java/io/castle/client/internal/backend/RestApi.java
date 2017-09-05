package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.Review;
import io.castle.client.model.Verdict;

public interface RestApi {

    /**
     * Send a async track request and provide a callback that inform if the request success or fail.
     *
     * @param event                event name
     * @param userId               unique userId
     * @param contextPayload       context json
     * @param propertiesPayload    properties json
     * @param asyncCallbackHandler callback to inform if request is correctly send
     */
    void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     * Sync call to the authenticate endpoint.
     * <p>
     * This method will block the current thread until the response is obtained.
     *
     * @param event             event name
     * @param userId            unique userId
     * @param contextPayload    context json
     * @param propertiesPayload properties json
     * @param traitsPayload     traits json
     * @return AuthenticateAction enum value to be used on login logic
     */
    Verdict sendAuthenticateSync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, JsonElement traitsPayload);

    /**
     * Async version of the authentication endpoint.
     * <p>
     * This method will return immediately and the response will be passed to the asyncCallbackHandler in the future.
     *
     * @param event                event name
     * @param userId               unique userId
     * @param contextPayload       context json
     * @param propertiesPayload    properties json
     * @param traitsPayload        traits json
     * @param asyncCallbackHandler callback to pass the AuthenticateAction enum value to be used on login logic
     */
    void sendAuthenticateAsync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, JsonElement traitsPayload, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Async call to the identify endpoint, returning immediately.
     *
     * @param userId      unique userId
     * @param contextJson context json
     * @param active      is this call realized as part of a active session of the user
     * @param traitsJson  additional trait json
     */
    void sendIdentifyRequest(String userId, JsonObject contextJson, boolean active, JsonElement traitsJson);

    /**
     * Sync call to the review endpoint.
     *
     * @param reviewId string representing the id to be reviewed
     * @return         a {@code review} with metadata contained in the body of the response
     */
    Review sendReviewRequestSync(String reviewId);

    /**
     * Async call to the review endpoint, returning immediately.
     *
     * @param reviewId        string representing the id to be reviewed
     * @param callbackHandler callback to handle the Review value returned by the API
     */
    void sendReviewRequestAsync(String reviewId, AsyncCallbackHandler<Review> callbackHandler);
}
