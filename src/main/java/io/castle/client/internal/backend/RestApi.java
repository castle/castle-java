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
     * Sync call to archive devices endpoint
     *
     * @param userId  string representing the user to archive devices for
     * @return a {@code user} with metadata contained in the body of the response
     */
    CastleUser sendArchiveUserDevicesRequestSync(String userId);

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
}
