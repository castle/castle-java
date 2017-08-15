package io.castle.client.internal.backend;

import com.google.gson.JsonElement;
import io.castle.client.internal.model.AuthenticateAction;

import java.util.concurrent.Future;

public interface RestApi {

    int sendTrackRequest(String event, String userId, JsonElement contextPayload,JsonElement propertiesPayload);

    AuthenticateAction sendAuthenticateSync(String event, String userId, JsonElement contextPayload,JsonElement propertiesPayload);

    Future<AuthenticateAction> sendAuthenticateAsync(String payload);

}
