package io.castle.client;

import com.google.common.hash.HashFunction;
import io.castle.client.api.CastleApi;
import io.castle.client.internal.CastleApiImpl;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.CastlePayloadBuilder;
import io.castle.client.model.CastleSdkConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Creates an instance of the Castle SDK
 *
 * This also provides methods for initialization of the SDK.
 * {@code this#initialize()} must be called once per instance of the SDK
 *
 * Static method {@code this#setSingletonInstance()} can be called to set a global instance of the SDK.
 * Once set the {@code this#instance()} method will return that instance
 */
public class Castle {
    public static final Logger logger = LoggerFactory.getLogger(Castle.class);

    private final CastleSdkInternalConfiguration internalConfiguration;

    /**
     * Public constructor for creating an instance of the SDK
     * <p>
     * Please use the static setSingletonInstance() and instance() methods to set and get an SDK instance.
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
    public static Castle instance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Castle SDK must be initialized. Call `Castle.initialize()` first");
        }
        return instance;
    }

    /**
     * Sets the SDK singleton instance.
     *
     * @param sdk configured instance of the sdk
     */
    public static void setSingletonInstance(Castle sdk) {
        if (instance == null) {
            instance = sdk;
        }
    }

    /**
     * Creates a API client instance for sending a request
     * @return A new instance of the API client {@code CastleApiImpl}
     * @throws IllegalStateException when the SDK has not been properly initialized
     */
    public CastleApi client() throws IllegalStateException {
        return buildApiClient(false);
    }

    /**
     * Creates a API client instance for sending a request
     * @param doNotTrack when true, the API calls will be not realized and default values will be provided
     * @return A new instance of the API client {@code CastleApiImpl}
     * @throws IllegalStateException when the SDK has not been properly initialized
     */
    public CastleApi client(boolean doNotTrack) throws IllegalStateException {
        return buildApiClient(doNotTrack);
    }

    /**
     * Verifies the SDK's internalConfiguration and initializes its internal Configuration.
     * @return A new instance of the sdk {@code Castle}
     * @throws CastleSdkConfigurationException if the provided settings in the environment of classpath have invalid values
     */
    public static Castle verifySdkConfigurationAndInitialize() throws CastleSdkConfigurationException {
        return initializeSDK();
    }


    private static synchronized Castle initializeSDK() throws CastleSdkConfigurationException {
        CastleSdkInternalConfiguration loadedConfig = CastleSdkInternalConfiguration.getInternalConfiguration();
        return new Castle(loadedConfig);
    }

    /**
     * Initialize and configure the Castle SDK using a configuration object
     *
     * @param config CastleConfiguration object
     * @return A new instance of the sdk {@code Castle}
     * @throws CastleSdkConfigurationException Configuration options missing or
     *   invalid
     */
    public static synchronized Castle initialize(CastleConfiguration config) throws CastleSdkConfigurationException {
        return new Castle(CastleSdkInternalConfiguration.buildFromConfiguration(config));
    }

    /**
     * Initialize the Castle SDK using default settings and variables from ENV
     *
     * @return A new instance of the sdk {@code Castle}
     * @throws CastleSdkConfigurationException Configuration options missing or
     *   invalid
     */
    public static Castle initialize() throws CastleSdkConfigurationException {
        return initialize(configurationBuilder().build());
    }

    /**
     * Initialize the Castle SDK using only the API secret key
     * @param  secret                          API Secret
     * @return A new instance of the sdk {@code Castle}
     * @throws CastleSdkConfigurationException Configuration options missing or
     *   invalid
     */
    public static Castle initialize(String secret) throws CastleSdkConfigurationException {
        return initialize(configurationBuilder().apiSecret(secret).build());
    }

    public static CastleConfigurationBuilder configurationBuilder() {
        return CastleSdkInternalConfiguration.builderFromConfigurationLoader();
    }

    /**
     * Returns a new builder object for constructing a CastlePayload
     * @return Return CastlePayloadBuilder object
     */
    public CastlePayloadBuilder payloadBuilder() {
        return buildPayloadBuilder();
    }

    /**
     * Create a new instance of a request payload builder
     * @return a new instance of {@code CastlePayloadBuilder}
     */
    public CastlePayloadBuilder buildPayloadBuilder() {
        return new CastlePayloadBuilder(
                getSdkConfiguration(),
                getGsonModel()
        );
    }

    /**
     * Returns a new builder object for constructing a CastleContext
     * @return Return CastleContextBuilder object
     */
    public CastleContextBuilder contextBuilder() {
        return buildContextBuilder();
    }

    /**
     * Create a new instance of a request context builder
     * @return a new instance of {@code CastleContextBuilder}
     */
    public CastleContextBuilder buildContextBuilder() {
        return new CastleContextBuilder(
                getSdkConfiguration(),
                getGsonModel()
        );
    }

    public CastleApi buildApiClient() {
        return buildApiClient(false);
    }

    public CastleApi buildApiClient(boolean doNotTrack) {
        return new CastleApiImpl(internalConfiguration, doNotTrack);
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
     * Get Gson model for serialization and deserialization
     * @return the Gson model
     */
    private CastleGsonModel getGsonModel() {
        return internalConfiguration.getModel();
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
