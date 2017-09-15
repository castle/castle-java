package io.castle.client.model;

/**
 * Exception thrown when the configuration settings have wrong values.
 */
public class CastleSdkConfigurationException extends Exception {

    public CastleSdkConfigurationException(String message) {
        super(message);
    }
}
