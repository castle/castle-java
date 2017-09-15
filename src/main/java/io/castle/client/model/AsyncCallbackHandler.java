package io.castle.client.model;

/**
 * Callback interface used to handle async requests responses.
 * <p>
 * An implementation of this  interface allows the user to handle async request success and failure cases in a custom manner.
 *
 * @param <T> The type of the internal response after execution.
 */
public interface AsyncCallbackHandler<T> {
    void onResponse(T response);

    void onException(Exception exception);
}
