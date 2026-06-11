package io.castle.client.model;

import okhttp3.Response;

/**
 * Runtime exception wrapping errors raised while calling the Castle API.
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
