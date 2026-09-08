package io.castle.client.model;

import okhttp3.Response;

/**
 * Unchecked exception thrown when a Castle API call fails.
 */
public class CastleRuntimeException extends RuntimeException {

    public CastleRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public CastleRuntimeException(String message) {
        super(message);
    }

    public CastleRuntimeException(Response response) {
        super(response.toString());
    }
}
