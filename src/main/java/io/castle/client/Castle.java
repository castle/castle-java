package io.castle.client;

import io.castle.client.api.CastleApi;
import io.castle.client.api.CastleApiImpl;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class Castle {
    public static final Logger logger = LoggerFactory.getLogger(Castle.class);

    private static Castle instance = new Castle();

    private final CastleSdkInternalConfiguration configuration;

    private Castle() {
        // Load configuration
        configuration = CastleSdkInternalConfiguration.getInternalConfiguration();
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
        return new CastleApiImpl(request, doNotTrack, configuration);
    }


}
