package io.castle.client.internal.config;

import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.model.AuthenticateFailoverStrategy;

import java.util.List;

/**
 * Application level settings used by the SDK singleton.
 * <p>
 * TODO: should we add support for overwriting some settings on a request basis???
 * Configuration values will be loaded from the environment or from the class path by a call to {@link ConfigurationLoader#loadConfiguration()}.
 */
public class CastleConfiguration {

    private final String apiBaseUrl;
    private final int timeout;

    private final AuthenticateFailoverStrategy authenticateFailoverStrategy;

    private final List<String> whiteListHeaders;

    private final List<String> blackListHeaders;

    private final String apiSecret;
    private final String castleAppId;
    private final CastleBackendProvider backendProvider;

    public CastleConfiguration(String apiBaseUrl, int timeout, AuthenticateFailoverStrategy authenticateFailoverStrategy, List<String> whiteListHeaders, List<String> blackListHeaders, String apiSecret, String castleAppId, CastleBackendProvider backendProvider) {
        this.apiBaseUrl = apiBaseUrl;
        this.timeout = timeout;
        this.authenticateFailoverStrategy = authenticateFailoverStrategy;
        this.whiteListHeaders = whiteListHeaders;
        this.blackListHeaders = blackListHeaders;
        this.apiSecret = apiSecret;
        this.castleAppId = castleAppId;
        this.backendProvider = backendProvider;
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

    public String getCastleAppId() {
        return castleAppId;
    }

    public CastleBackendProvider getBackendProvider() {
        return backendProvider;
    }

}
