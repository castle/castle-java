package io.castle.client.model;

/**
 * Exception thrown if the client is configured to use a THROW {@code AuthenticateFailoverStrategy} and
 * if a call to the authenticate endpoint of the Castle API fails.
 */
public class CastleRuntimeException extends RuntimeException {

    public CastleRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
