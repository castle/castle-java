package io.castle.client.model;

/**
 * Callback interface used to handle async requests responses.
 *
 * @param <T> The type of the internal response type after execution.
 */
public interface AsyncCallbackHandler<T> {
    void onResponse(T response);

    void onException(Exception exception);
}
