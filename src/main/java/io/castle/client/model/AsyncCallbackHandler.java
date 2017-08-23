package io.castle.client.model;

/**
 * Callback interface used to handle async requests responses.
 * <p>
 * Failure responses are handled using default values or with special values of the response Type, see documentation on methods where this interface is used.
 *
 * @param <T> The type of the internal response type after execution.
 */
public interface AsyncCallbackHandler<T> {
    void onResponse(T response);
}
