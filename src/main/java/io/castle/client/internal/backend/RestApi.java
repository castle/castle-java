package io.castle.client.internal.backend;

import io.castle.client.internal.model.AuthenticateAction;

import java.util.concurrent.Future;

public interface RestApi {

    int sendTrackRequest(String event, String userId, String contextPayload,String propertiesPayload);

    AuthenticateAction sendAuthenticateSync(String event, String userId, String contextPayload,String propertiesPayload);

    Future<AuthenticateAction> sendAuthenticateAsync(String payload);

}
