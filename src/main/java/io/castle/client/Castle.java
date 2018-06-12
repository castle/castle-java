package io.castle.client;

import com.google.common.hash.HashFunction;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.CastleApiImpl;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Through static methods, creates a singleton instance of this class, which provides instances of {@code CastleAPI}
 * and keeps in its internal configuration all application level settings.
 *
 * This also provides methods for initialization of the SDK.
 * {@code this#verifySdkConfigurationAndInitialize()} must be called once in the lifetime of an application using the SDK
 * during its initialization process.
 *
 * After initialization, {@code this#sdk()} will return the singleton instance of this class, which grants access to
 * its non-static methods.
 */
public class Castle {
    public static final Logger logger = LoggerFactory.getLogger(Castle.class);

    private final CastleSdkInternalConfiguration internalConfiguration;

    /**
     * Public constructor for test proposes only.
     * <p>
     * Please use the static sdk() method to get the SDK instance.
     *
     * @param internalConfiguration a test internal configuration for the SDK
     */
    public Castle(CastleSdkInternalConfiguration internalConfiguration) {
        this.internalConfiguration = internalConfiguration;
    }

    private static Castle instance;

    /**
     * Gets the SDK singleton instance.
     *
     * @return the singleton instance of {@code this}
     * @throws IllegalStateException when the SDK has not been properly initialized
     */
    public static Castle sdk() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Castle SDK must be initialized via a call to verifySdkConfigurationAndInitialize");
        }
        return instance;
    }

    /**
     * Verifies the SDK's internalConfiguration and initializes its internal Configuration.
     *
     * @throws CastleSdkConfigurationException if the provided settings in the environment of classpath have invalid values
     */
    public static void verifySdkConfigurationAndInitialize() throws CastleSdkConfigurationException {
        initializeSDK();
    }

    private static synchronized void initializeSDK() throws CastleSdkConfigurationException {
        CastleSdkInternalConfiguration loadedConfig = CastleSdkInternalConfiguration.getInternalConfiguration();
        instance = new Castle(loadedConfig);
    }

    /**
     * Initialize and configure the Castle SDK using a configuration builder object
     *
     * @param config CastleConfiguration object
     * @throws CastleSdkConfigurationException Configuration options missing or
     *   invalid
     */
    public static synchronized void initialize(CastleConfiguration config) throws CastleSdkConfigurationException {
        instance = new Castle(
            CastleSdkInternalConfiguration.buildFromConfiguration(config)
        );
    }

    /**
     * Initialize the Castle SDK using default settings and variables from ENV
     *
     * @throws CastleSdkConfigurationException Configuration options missing or
     *   invalid
     */
    public static void initialize() throws CastleSdkConfigurationException {
        initialize(configurationBuilder().build());
    }

    public static CastleConfigurationBuilder configurationBuilder() {
        return CastleSdkInternalConfiguration.builderFromConfigurationLoader();
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
     * Gets the SDK's configuration stored in an instance of {@code CastleConfiguration}.
     *
     * @return the SDK's configuration
     */
    public CastleConfiguration getSdkConfiguration() {
        return internalConfiguration.getConfiguration();
    }

    /**
     * Package accessible method for testing purposes.
     *
     * @return the internal sdk configuration
     */
    CastleSdkInternalConfiguration getInternalConfiguration() {
        return internalConfiguration;
    }

    /**
     * Calculate the secure userId HMAC using the internal API Secret.
     * @param userId raw user Id
     * @return the HMAC of the userId
     */
    public String secureUserID(String userId) {
        HashFunction hashFunction = internalConfiguration.getSecureHashFunction();
        return hashFunction.hashString(userId,com.google.common.base.Charsets.UTF_8).toString();
    }
}
