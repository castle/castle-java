package io.castle.client.api;

import io.castle.client.internal.model.AuthenticateAction;

public interface CastleApi {

    /**
     * Merge a additional context model to the default castle.io context.
     *
     * @param additionalContext A client defined model
     * @return a API reference with the merged context values.
     */
    CastleApi mergeContext(Object additionalContext);

    AuthenticateAction authenticate(String event, String userId);

    AuthenticateAction authenticate(String event, String userId, Object properties);

    void track(String event);

    void track(String event, String userId);

    void track(String event, String userId, Object properties);

    void identify(String userId, boolean active, Object traits);


}
