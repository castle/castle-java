package io.castle.client.model;

import okhttp3.Response;

/**
 * Exception thrown if the client is configured to use a THROW
 * {@code AuthenticateFailoverStrategy} and if a call to the authenticate endpoint of the Castle API fails.
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
