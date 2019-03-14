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
     * Async call to the identify endpoint, returning immediately.
     *
     * @param userId      unique userId
     * @param contextJson context json
     * @param active      is this call realized as part of a active session of the user
     * @param traitsJson  additional traits json
     */
    void sendIdentifyRequest(String userId, JsonObject contextJson, boolean active, JsonElement traitsJson);

    /**
     * Sync call to the review endpoint.
     *
     * @param reviewId string representing the id to be reviewed
     * @return a {@code review} with metadata contained in the body of the response
     */
    Review sendReviewRequestSync(String reviewId);

    /**
     * Async call to the review endpoint, returning immediately.
     *
     * @param reviewId        string representing the id to be reviewed
     * @param callbackHandler callback to handle the Review value returned by the API
     */
    void sendReviewRequestAsync(String reviewId, AsyncCallbackHandler<Review> callbackHandler);

    CastleUserDevice sendApproveDeviceRequestSync(String deviceToken);

    void sendApproveDeviceRequestAsync(String deviceToken, AsyncCallbackHandler<CastleUserDevice> callbackHandler);

    CastleUserDevice sendReportDeviceRequestSync(String deviceToken);

    void sendReportDeviceRequestAsync(String deviceToken, AsyncCallbackHandler<CastleUserDevice> callbackHandler);

    CastleUserDevices sendGetUserDevicesRequestSync(String userId);

    void sendGetUserDevicesRequestAsync(String userId, AsyncCallbackHandler<CastleUserDevices> callbackHandler);

    CastleUserDevice sendGetUserDeviceRequestSync(String deviceToken);

    void sendGetUserDeviceRequestAsync(String userId, AsyncCallbackHandler<CastleUserDevice> callbackHandler);
}
