package io.castle.client.internal.config;

import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleRuntimeException;

import java.util.List;

/**
 * Application level settings used by the SDK singleton.
 * <p>
 * Configuration values will be loaded from the environment or from the class path by a call to
 * {@link ConfigurationLoader#loadConfiguration()}.
 */
public class CastleConfiguration {

    /**
     * Endpoint of the Castle API.
     */
    private final String apiBaseUrl;

    /**
     * Timeout after which a request fails.
     */
    private final int timeout;

    /**
     * Strategy for returning a {@code verdict} when an authenticate call fails.
     */
    private final AuthenticateFailoverStrategy authenticateFailoverStrategy;

    /**
     * List of headers that will get passed to the {@code CastleContext} unless they are blacklisted.
     */
    private final List<String> whiteListHeaders;

    /**
     * List of headers that will never get passed to the {@code CastleContext} when built from an HTTP request.
     */
    private final List<String> blackListHeaders;

    /**
     * Secret associated to a Castle account.
     */
    private final String apiSecret;

    /**
     * Identifier used for authentication purposes for requests to the Castle API.
     */
    private final String castleAppId;

    /**
     * HTTP layer that will be used for calls to the Castle API
     */
    private final CastleBackendProvider backendProvider;

    /**
     * Flag to add logging information on HTTP backend.
     */
    private final boolean logHttpRequests;

    public CastleConfiguration(String apiBaseUrl, int timeout, AuthenticateFailoverStrategy authenticateFailoverStrategy, List<String> whiteListHeaders, List<String> blackListHeaders, String apiSecret, String castleAppId, CastleBackendProvider backendProvider, boolean logHttpRequests) {
        this.apiBaseUrl = apiBaseUrl;
        this.timeout = timeout;
        this.authenticateFailoverStrategy = authenticateFailoverStrategy;
        this.whiteListHeaders = whiteListHeaders;
        this.blackListHeaders = blackListHeaders;
        this.apiSecret = apiSecret;
        this.castleAppId = castleAppId;
        this.backendProvider = backendProvider;
        this.logHttpRequests = logHttpRequests;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public AuthenticateFailoverStrategy getAuthenticateFailoverStrategy() {
        return authenticateFailoverStrategy;
    }

    public List<String> getWhiteListHeaders() {
        return whiteListHeaders;
    }

    public List<String> getBlackListHeaders() {
        return blackListHeaders;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * Gets the App Id, when it exists
     *
     * @return A string representing the AppID
     * @throws CastleRuntimeException when there is no App Id
     */
    public String getCastleAppId() throws CastleRuntimeException {
        if (castleAppId == null || castleAppId.isEmpty()) {
            throw new CastleRuntimeException("AppId was not specified");
        } else {
            return castleAppId;
        }
    }

    public CastleBackendProvider getBackendProvider() {
        return backendProvider;
    }

    public boolean isLogHttpRequests() {
        return logHttpRequests;
    }
}
