package io.castle.client.api;

import io.castle.client.model.AuthenticateAction;

import javax.annotation.Nullable;

public interface CastleApi {

    /**
     * Merge a additional context model to the default castle.io context.
     *
     * @param additionalContext A client defined model
     * @return a API reference with the merged context values.
     */
    CastleApi mergeContext(Object additionalContext);

    AuthenticateAction authenticate(String event, String userId);

    AuthenticateAction authenticate(String event, String userId, @Nullable Object properties);

    /**
     *
     * @param event TODO!!!!!!!!!!
     */
    void track(String event);

    /**
     *
     * @param event TODO!!!!!!!!!!
     * @param userId TODO!!!!!!!!!!
     */
    void track(String event, @Nullable String userId);

    /**
     *
     * @param event TODO!!!!!!!!!!
     * @param userId TODO!!!!!!!!!!
     * @param properties TODO!!!!!!!!!!
     */
    void track(String event, @Nullable String userId, @Nullable Object properties);

    /**
     * Call to the identify endpoint @see <a href="https://api.castle.io/docs#identify">The docs</a>
     *
     * @param userId user unique ID
     * @param active is this call realized in an active user session
     * @param traits additional traits parameters to send
     * @param properties additional properties parameters to send
     */
    void identify(String userId, boolean active, @Nullable Object traits, @Nullable Object properties);

}
