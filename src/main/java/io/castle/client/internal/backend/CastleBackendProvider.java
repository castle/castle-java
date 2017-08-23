package io.castle.client.internal.backend;

/**
 * List of backend providers available for the HTTP layer.
 *
 * The default value is OKHTTP.
 */
public enum CastleBackendProvider {
    OKHTTP, DUMP
}
