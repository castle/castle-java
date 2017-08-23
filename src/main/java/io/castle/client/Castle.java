package io.castle.client;

import io.castle.client.api.CastleApi;
import io.castle.client.api.CastleApiImpl;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class Castle {
    public static final Logger logger = LoggerFactory.getLogger(Castle.class);

    private final CastleSdkInternalConfiguration configuration;

    public Castle(CastleSdkInternalConfiguration configuration) {
        this.configuration = configuration;
    }


    private static Castle instance;

    public static Castle sdk() {
        if (instance != null) {
            try {
                initializeSDK();
            } catch (CastleSdkConfigurationException e) {
                throw new IllegalStateException("Castle SDK initialization failure. Ensure the the configuration is correct and that the verifySdkConfiguration method is called during application initialization");
            }
        }
        return instance;
    }

    /**
     * Verify SDK configuration and initialize the internal configuration.
     */
    public static void verifySdkConfiguration() throws CastleSdkConfigurationException {
        initializeSDK();
    }

    private static synchronized void initializeSDK() throws CastleSdkConfigurationException {
        CastleSdkInternalConfiguration loadedConfig = CastleSdkInternalConfiguration.getInternalConfiguration();
        instance = new Castle(loadedConfig);
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
