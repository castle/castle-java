package io.castle.client;

import io.castle.client.api.CastleApi;
import io.castle.client.api.CastleApiImpl;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Makes it possible to create an instance of {@code CastleApi} with the proper configuration, an HTTP layer and a
 * context object while verifying that the client has been properly configured.
 * <p>
 * Through static methods, creates a singleton instance of this class which keeps in its internal configuration
 * application the application level settings loaded from
 *
 *
 */
public class Castle {
    public static final Logger logger = LoggerFactory.getLogger(Castle.class);

    private final CastleSdkInternalConfiguration internalConfiguration;

    public Castle(CastleSdkInternalConfiguration internalConfiguration) {
        this.internalConfiguration = internalConfiguration;
    }

    private static Castle instance;

    /**
     * Gets the SDK singleton instance.
     *
     * @return
     * @throws IllegalStateException
     */
    public static Castle sdk() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Castle SDK must be initialized via a call to verifySdkConfigurationAndInitialize");
        }
        return instance;
    }

    /**
     * Verify SDK internalConfiguration and initialize the internal Configuration.
     */
    public static void verifySdkConfigurationAndInitialize() throws CastleSdkConfigurationException {
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
        return new CastleApiImpl(request, doNotTrack, internalConfiguration);
    }

    /**
     *
     * @return
     */
    public CastleConfiguration getSdkConfiguration() {
        return internalConfiguration.getConfiguration();
    }

    /**
     * Package accessible method for testing purposes.
     * @return the internal sdk configuration
     */
    CastleSdkInternalConfiguration getInternalConfiguration() {
        return internalConfiguration;
    }
}
