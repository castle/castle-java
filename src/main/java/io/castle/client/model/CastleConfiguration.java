package io.castle.client.model;

import java.util.List;

public class CastleConfiguration {

    private final int timeout;

    /**
     * The authentication strategy in case of timeout on response.
     * If null, then the timeout exception will be throw.
     */
    private final AuthenticateStrategy failoverStrategy;

    private final List<String> whiteListHeaders;
    private final List<String> blackListHeaders;

    private final String apiSecret;

    public CastleConfiguration(int timeout, AuthenticateStrategy failoverStrategy, List<String> whiteListHeaders, List<String> blackListHeaders, String apiSecret) {
        this.timeout = timeout;
        this.failoverStrategy = failoverStrategy;
        this.whiteListHeaders = whiteListHeaders;
        this.blackListHeaders = blackListHeaders;
        this.apiSecret = apiSecret;
    }

    public int getTimeout() {
        return timeout;
    }

    public AuthenticateStrategy getFailoverStrategy() {
        return failoverStrategy;
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
}
