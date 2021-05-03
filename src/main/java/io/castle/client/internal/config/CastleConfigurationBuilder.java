package io.castle.client.internal.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.internal.utils.HeaderNormalizer;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;

import java.util.LinkedList;
import java.util.List;

/**
 * Allows to programmatically create and validate a castleConfiguration through a DSL.
 * <p>
 * CastleConfigurationBuilder has methods for creating a castleConfiguration instance from scratch.
 * Furthermore, there are also methods for creating a configuration from sensible default values.
 * The fields that can be set in a CastleConfiguration:
 * <ul>
 * <li> timeout
 * <li> failoverStrategy
 * <li> whiteListHeaders
 * <li> blackListHeaders
 * <li> apiSecret
 * <li> castleAppId
 * <li> backendProvider
 * </ul>
 * The {@code build} method provides a layer of validation.
 * It will throw a {@link CastleSdkConfigurationException} if one of the fields is left unset.
 * {@code apiSecret} and {@code castleAppId} do not have defaults.
 * A value for the Api Secret should be provided before calling {@code  build}.
 * <h2>How to use Whitelist and Blacklist</h2>
 * When {@link io.castle.client.Castle#onRequest} is called, whitelisted headers and blacklisted headers are checked.
 * Headers are only passed to the context if they are in the whitelist, but not in the blacklist.
 * That is, if a header is in both lists, the blacklisted value takes precedence, and the header is not passed to the
 * context object.
 */
public class CastleConfigurationBuilder {
    /**
     * Represents the milliseconds after which a request fails.
     */
    private int timeout = 500;

    /**
     * Strategy used when an authenticate call to the Castle API fails.
     */
    private AuthenticateFailoverStrategy failoverStrategy;

    /**
     * Strings representing headers that should be passed to the context object unless they are also blacklisted.
     */
    private List<String> whiteListHeaders;

    /**
     * Strings representing headers that should never be passed to the context object.
     */
    private List<String> blackListHeaders;

    /**
     * String represented the API secret associated with a Castle account.
     */
    private String apiSecret;

    /**
     * String representing an AppID associated with a Castle account.
     */
    private String castleAppId;

    /**
     * The HTTP layer chosen to make requests.
     */
    private CastleBackendProvider backendProvider = CastleBackendProvider.OKHTTP; //Unique available backend on the current version

    /**
     * Base URL of the API calls. Used for tests, on productions use the default value.
     */
    private String apiBaseUrl;

    /**
     * Flag to enable logging of backend HTTP requests.
     */
    private boolean logHttpRequests = false;

    /**
     * Sorted list of headers to use for IP value in context
     */
    private List<String> ipHeaders;

    /**
     * Max number of simultaneous requests to Castle
     */
    private int maxRequests = 5;

    private CastleConfigurationBuilder() {
    }

    /**
     * Provides a castleConfigurationBuilder with the default values set for all of CastleConfiguration's fields but
     * apiSecret and castleAppId.
     * <p>
     * Values for {@link CastleConfiguration#apiSecret} and {@link CastleConfiguration#castleAppId} must be provided
     * before calling {@code build}.
     * These two fields do not have default values.
     *
     * @return a castleConfigurationBuilder with all settings set to the default values, when there exist defaults
     */
    public static CastleConfigurationBuilder defaultConfigBuilder() {
        CastleConfigurationBuilder builder = new CastleConfigurationBuilder()
                .withDefaultWhitelist()
                .withDefaultBlacklist()
                .withDefaultApiBaseUrl()
                .withTimeout(500)
                .withDefaultAuthenticateFailoverStrategy()
                .withDefaultBackendProvider()
                .withMaxRequests(5);
        return builder;
    }

    /**
     * Provides a fresh castleConfigurationBuilder.
     * <p>
     * The only default value provided is the timeout, which is set to 500 milliseconds.
     *
     * @return a castleConfigurationBuilder with all values set to null, except timeout
     */
    public static CastleConfigurationBuilder aConfigBuilder() {
        return new CastleConfigurationBuilder();
    }

    /**
     * Sets the default list of whitelisted headers.
     *
     * @return a castleConfigurationBuilder instance with the default list of whitelisted headers.
     */
    public CastleConfigurationBuilder withDefaultWhitelist() {
        this.whiteListHeaders = new LinkedList<>();
        return this;
    }

    /**
     * Sets the default list of blacklisted headers.
     * <p>
     * The default value is a list whose single element is the {@code Cookie} header.
     *
     * @return a castleConfigurationBuilder instance with the default list of blacklisted headers
     */
    public CastleConfigurationBuilder withDefaultBlacklist() {
        this.blackListHeaders = new LinkedList<>();
        this.blackListHeaders.add("Cookie");
        this.blackListHeaders.add("Authorization");
        return this;
    }

    /**
     * Sets the timeout in milliseconds for a request.
     *
     * @param timeout milliseconds after which a request times out
     * @return a castleConfigurationBuilder with a timeout set to a new value
     */
    public CastleConfigurationBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Establishes the authentication strategy that will be used in case of a timeout when performing a
     * {@code CastleApi#authenticate} call.
     *
     * @param failoverStrategy strategy to use for failed authenticate API calls; not null.
     * @return a castleConfigurationBuilder with the chosen AuthenticationStrategy set
     */
    public CastleConfigurationBuilder withAuthenticateFailoverStrategy(AuthenticateFailoverStrategy failoverStrategy) {
        this.failoverStrategy = failoverStrategy;
        return this;
    }

    /**
     * Sets the failover strategy for the authenticate Castle API call to allow.
     * <p>
     * The authenticate failover strategy for the default configuration is to return {@link AuthenticateAction#ALLOW}.
     *
     * @return a castleConfigurationBuilder with allow as the authenticate failover strategy
     */
    public CastleConfigurationBuilder withDefaultAuthenticateFailoverStrategy() {
        return this.withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.ALLOW));
    }

    /**
     * Sets the endpoint of the Castle API to its default value.
     * <p>
     * The default value is {@code https://api.castle.io/}.
     * @return a castleConfigurationBuilder with the default value for the Castle API endpoint
     */
    public CastleConfigurationBuilder withDefaultApiBaseUrl() {
        return this.withApiBaseUrl("https://api.castle.io/");
    }

    /**
     * Sets the OkHttp backend for all API calls.
     *
     * @return a castleConfigurationBuilder with the backend set to okHttp
     */
    public CastleConfigurationBuilder withDefaultBackendProvider() {
        return this.withBackendProvider(CastleBackendProvider.OKHTTP);
    }

    /**
     * Sets a max number of simultaneous requests to the Castle API
     *
     * @param maxRequests max number of connections to Castle
     * @return a castleConfigurationBuilder with a max request count
     */
    public CastleConfigurationBuilder withMaxRequests(Integer maxRequests) {
        this.maxRequests = maxRequests;
        return this;
    }

    /**
     * Whitelists a list of strings representing headers that will be passed to the context object from an
     * {@code HttpServletRequest}.
     *
     * @param whitelistedHeaders a list of strings representing headers; case insensitive, not null.
     * @return a castleConfigurationBuilder with a whitelist of headers for building context objects
     */
    public CastleConfigurationBuilder withWhiteListHeaders(List<String> whitelistedHeaders) {
        this.whiteListHeaders = whitelistedHeaders;
        return this;
    }

    /**
     * Whitelists a a comma separated list of string parameters representing headers that will be passed to the context
     * object from an {@code HttpServletRequest}.
     *
     * @param whitelistedHeaders a comma separated list of string parameters representing headers; case insensitive, not
     *                           null.
     * @return a castleConfigurationBuilder with a whitelist of headers for building context objects
     */
    public CastleConfigurationBuilder withWhiteListHeaders(String... whitelistedHeaders) {
        return withWhiteListHeaders(ImmutableList.copyOf(whitelistedHeaders));
    }

    /**
     * Blacklists a a comma separated list of string parameters representing headers that will be passed to the context
     * object from an {@code HttpServletRequest}.
     *
     * @param blacklistedHeaders a comma separated list of string parameters representing headers; case insensitive, not
     *                           null.
     * @return a castleConfigurationBuilder with a blacklist of headers for building context objects
     */
    public CastleConfigurationBuilder withBlackListHeaders(String... blacklistedHeaders) {
        return withBlackListHeaders(ImmutableList.copyOf(blacklistedHeaders));
    }

    /**
     * Blacklists a list of strings representing headers that will be passed to the context object from an
     * {@code HttpServletRequest}.
     *
     * @param blacklistedHeaders a list of strings representing headers; case insensitive, not null.
     * @return a castleConfigurationBuilder with a blacklist of headers for building context objects
     */
    public CastleConfigurationBuilder withBlackListHeaders(List<String> blacklistedHeaders) {
        this.blackListHeaders = blacklistedHeaders;
        return this;
    }

    /**
     * A string with the API secret associated with a valid Castle account.
     *
     * @param apiSecret the API secret of the account calling the Castle API
     * @return a castleConfigurationBuilder with the API secret set
     */
    public CastleConfigurationBuilder withApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    /* alias for withApiSecret  */
    public CastleConfigurationBuilder apiSecret(String apiSecret) {
        return withApiSecret(apiSecret);
    }

    /**
     * Validates and returns a configuration object, if all required fields have the required values, or an exception
     * otherwise.
     * <p>
     * Validation consists in checking that no field in the configuration is set tu null.
     * Furthermore, the build method also makes sure that the API secret is not an empty string.
     * An additional layer of validation is given further along the line by {@link HeaderNormalizer}, which will ensure
     * that the provided list of strings can contain both, upper and lower case letters.
     * This method will not detect wrong headers, API secrets or APP ID's.
     *
     * @return a castleConfiguration with all fields set to some meaningful value
     * @throws CastleSdkConfigurationException if at least one of castleAppId, apiSecret, whiteListHeaders,
     *                                         blackListHeaders, failoverStrategy, backendProvider is not provided
     *                                         during the building stage of the
     *                                         CastleConfiguration instance.
     */
    public CastleConfiguration build() throws CastleSdkConfigurationException {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (apiSecret == null || apiSecret.isEmpty()) {
            builder.add("The apiSecret for the castleSDK must be provided in the configuration. Read documentation " +
                    "for further details.");
        }
        if (whiteListHeaders == null) {
            builder.add("A whitelist of headers must be provided. If not sure, then use the default values provided " +
                    "by method withDefaultWhitelist. Read documentation for further details.");
        }
        if (blackListHeaders == null) {
            builder.add("A blacklist of headers must be provided. If not sure, then use the default values provided " +
                    "by method withDefaultBlacklist. Read documentation for further details.");
        }
        if (failoverStrategy == null) {
            builder.add("A failover strategy must be provided. If not sure, then use the default values provided " +
                    "by method withDefaultAuthenticateFailoverStrategy. Read documentation for further details.");
        }
        if (backendProvider == null) {
            builder.add("A backend provider must be selected. If not sure, then use the default values provided " +
                    "by method withDefaultBackendProvider. Read documentation for further details.");
        }
        if (apiBaseUrl == null) {
            builder.add("A apiBaseUrl value must be selected. If not sure, then use the default values provided by method withDefaultApiBaseUrl. Read documentation for further details.");
        }
        ImmutableList<String> errorMessages = builder.build();
        if (!errorMessages.isEmpty()) {
            throw new CastleSdkConfigurationException(Joiner.on(System.lineSeparator()).join(errorMessages));
        }
        HeaderNormalizer normalizer = new HeaderNormalizer();
        return new CastleConfiguration(apiBaseUrl,
                timeout,
                failoverStrategy,
                normalizer.normalizeList(whiteListHeaders),
                normalizer.normalizeList(blackListHeaders),
                apiSecret,
                castleAppId,
                backendProvider,
                logHttpRequests,
                ipHeaders,
                maxRequests);
    }

    /**
     * Sets a String representing an AppID associated with a Castle account.
     *
     * @param castleAppId the AppID for that will be used to make calls to the Castle API
     * @return a castleConfigurationBuilder with the AppID set
     */
    public CastleConfigurationBuilder withCastleAppId(String castleAppId) {
        this.castleAppId = castleAppId;
        return this;
    }

    public CastleConfigurationBuilder appId(String appId) {
        return withCastleAppId(appId);
    }

    /**
     * Sets the HTTP layer that will be used for making calls to the Castle REST API.
     *
     * @param backendProvider an available backend provider
     * @return a castleConfigurationBuilder with the chosen backend provider set
     */
    public CastleConfigurationBuilder withBackendProvider(CastleBackendProvider backendProvider) {
        this.backendProvider = backendProvider;
        return this;
    }

    /**
     * Sets the endpoint of the Castle API.
     *
     * @param apiBaseUrl a string representing the URL of the Castle API endpoint without any relative path
     * @return a castleConfigurationBuilder with the default value for the Castle API endpoint
     */
    public CastleConfigurationBuilder withApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        return this;
    }

    /**
     * Flag to enable logging on backend HTTP requests.
     *
     * @param logHttpRequests boolean to switch logging on or off.
     * @return a castleConfigurationBuilder with logging setup set
     */
    public CastleConfigurationBuilder withLogHttpRequests(Boolean logHttpRequests) {
        this.logHttpRequests = logHttpRequests;
        return this;
    }

    /* alias for logHttpRequests */
    public CastleConfigurationBuilder enableHttpLogging(Boolean logHttpRequests) {
        return withLogHttpRequests(logHttpRequests);
    }

    /**
     * Sorted list of headers to use when extracting IP from headers.
     *
     * @param ipHeaders sorted list of headers.
     * @return a castleConfigurationBuilder with IP headers set
     */
    public CastleConfigurationBuilder withIPHeaders(List<String> ipHeaders) {
        this.ipHeaders = ipHeaders;
        return this;
    }

    /* alias for withIPHeaders */
    public CastleConfigurationBuilder ipHeaders(List<String> ipHeaders) {
        return withIPHeaders(ipHeaders);
    }
}