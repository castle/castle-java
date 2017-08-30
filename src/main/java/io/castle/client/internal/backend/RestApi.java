package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
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
     * Sync call to the authenticate endpoint. This method will block the current thread until the response is obtained.
     *
     * @param event             event name
     * @param userId            unique userId
     * @param contextPayload    context json
     * @param propertiesPayload properties json
     * @return AuthenticateAction enum value to be used on login logic.
     */
    Verdict sendAuthenticateSync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload);

    /**
     * Async version of the authentication endpoint. This method will return immediately and the response will be passed to the asyncCallbackHandler in the future.
     *
     * @param event                event name
     * @param userId               unique userId
     * @param contextPayload       context json
     * @param propertiesPayload    properties json
     * @param asyncCallbackHandler callback to pass the AuthenticateAction enum value to be used on login logic.
     */
    void sendAuthenticateAsync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Async call to the identify endpoint. This method will return immediately.
     *
     * @param userId            unique userId
     * @param contextJson       context json
     * @param active            is this call realized as part of a active session of the user
     * @param traitsJson        additional trait json
     */
    void sendIdentifyRequest(String userId, JsonObject contextJson, boolean active, JsonElement traitsJson);
}
