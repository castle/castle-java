package io.castle.client.internal.backend;

import io.castle.client.model.*;

public interface RestApi {

    /**
     * Make a GET request to a Castle API endpoint such as /v1/{userId}/devices
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse get(String path);

    /**
     * Make a POST request to a Castle API endpoint such as /v1/track
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse post(String path, Object payload);

    /**
     * Make a PUT request to a Castle API endpoint such as /v1/devices/{deviceToken}/report
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse put(String path);

    /**
     * Make a PUT request to a Castle API endpoint such as /v1/devices/{deviceToken}/report
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse put(String path, Object payload);

    /**
     * Make a DELETE request to a Castle API endpoint such as /v1/impersonate
     *
     * @param path api path
     * @return a decoded json response
     */
    CastleResponse delete(String path);

    /**
     * Make a DELETE request to a Castle API endpoint such as /v1/impersonate
     *
     * @param path api path
     * @param payload request payload
     * @return a decoded json response
     */
    CastleResponse delete(String path, Object payload);
}
