package io.castle.client;

import io.castle.client.api.CastleApi;
import io.castle.client.api.CastleApiImpl;

import javax.servlet.http.HttpServletRequest;

public class Castle {

    private static Castle instance = new Castle();

    private Castle() {
        // Load configuration
    }

    public static Castle sdk() {
        return instance;
    }

    /**
     * Create a API context for the given request.
     * Tracking is ON by default.
     *
     * @param request The request for data extraction
     * @return a API reference to make backend calls to the castle.io rest api.
     */
    public CastleApi onRequest(HttpServletRequest request) {
        return onRequest(request, false);
    }

    /**
     * Create a API context for the given request.
     *
     * @param request    The request for data extraction
     * @param doNotTrack when true, the API calls will be not realized and default values will be provided
     * @return a API reference to make backend calls to the castle.io rest api.
     */
    public CastleApi onRequest(HttpServletRequest request, boolean doNotTrack) {
        //TODO current implementation is a fake empty class
        return new CastleApiImpl();
    }


}
