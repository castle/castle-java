package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.castle.client.model.*;

public interface RestApi {

    /**
     *
     * @param payloadJson          JSON object containing the event properties
     * @param asyncCallbackHandler callback to inform if request was correctly sent
     */
    void sendTrackRequest(JsonElement payloadJson, AsyncCallbackHandler<Boolean> asyncCallbackHandler);

    /**
     *
     * @param payloadJson JSON object containing the event properties
     * @return Verdict to be used in login logic
     */
    Verdict sendAuthenticateSync(JsonElement payloadJson);

    /**
     *
     * @param payloadJson          JSON object containing the event properties
     * @param asyncCallbackHandler callback to inform if request was correctly sent
     */
    void sendAuthenticateAsync(JsonElement payloadJson, AsyncCallbackHandler<Verdict> asyncCallbackHandler);

    /**
     * Remove user from Castle (GDPR reasons)
     * @see <a href="https://castle.io/docs/gdpr_apis#user-data-purge-requests">The docs</a>
     * @param userId        user id to be removed
     */
    Boolean sendPrivacyRemoveUser(String userId);

    /**
     * Sync call to the approve device endpoint.
     *
     * @param deviceToken string representing the token for the device to get
     * @return a {@code device} with metadata contained in the body of the response
     */
    CastleUserDevice sendApproveDeviceRequestSync(String deviceToken);

    /**
     * Sync call to the report device endpoint.
     *
     * @param deviceToken string representing the token for the device to get
     * @return a {@code device} with metadata contained in the body of the response
     */
    CastleUserDevice sendReportDeviceRequestSync(String deviceToken);

    /**
     * Sync call to the devices endpoint.
     *
     * @param userId string representing the user to get devices for
     * @return a {@code devices} with metadata contained in the body of the response
     */
    CastleUserDevices sendGetUserDevicesRequestSync(String userId);

    /**
     * Sync call to the device endpoint.
     *
     * @param deviceToken string representing the token for the device to get
     * @return a {@code device} with metadata contained in the body of the response
     */
    CastleUserDevice sendGetUserDeviceRequestSync(String deviceToken);

    /**
     * Sync call to the impersonate endpoint.
     *
     * @param userId id of the user to impersonate
     * @param impersonator id of the user doing the impersonation
     * @param contextJson context json
     * @return a success message
     */
    CastleSuccess sendImpersonateStartRequestSync(String userId, String impersonator, JsonObject contextJson);

    /**
     * Sync call to the impersonate endpoint.
     *
     * @param userId id of the user to stop impersonating
     * @param impersonator id of the user doing the impersonation
     * @param contextJson context json
     * @return a success message
     */
    CastleSuccess sendImpersonateEndRequestSync(String userId, String impersonator, JsonObject contextJson);

    /**
     * Make a GET request to a Castle API endpoint such as /v1/{userId}/devices
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse get(String path);

    /**
     * Make a POST request to a Castle API endpoint such as /v1/track
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse post(String path, Object payload);

    /**
     * Make a PUT request to a Castle API endpoint such as /v1/devices/{deviceToken}/report
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse put(String path);

    /**
     * Make a PUT request to a Castle API endpoint such as /v1/devices/{deviceToken}/report
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse put(String path, Object payload);

    /**
     * Make a DELETE request to a Castle API endpoint such as /v1/impersonate
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse delete(String path);

    /**
     * Make a DELETE request to a Castle API endpoint such as /v1/impersonate
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse delete(String path, Object payload);
}
