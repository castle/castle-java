package io.castle.client.model;

public class CastleApiTimeoutException extends CastleRuntimeException {

    public CastleApiTimeoutException(Throwable throwable) {
        super(throwable);
    }

    public CastleApiTimeoutException(String message) {
        super(message);
    }
}
