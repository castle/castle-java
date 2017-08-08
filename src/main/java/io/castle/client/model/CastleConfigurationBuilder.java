package io.castle.client.model;

import com.google.common.base.Preconditions;

import java.util.LinkedList;
import java.util.List;

public class CastleConfigurationBuilder {
    private int timeout = 500;
    private AuthenticateStrategy failoverStrategy;
    private List<String> whiteListHeaders;
    private List<String> blackListHeaders;
    private String apiSecret;

    private CastleConfigurationBuilder() {
    }

    /**
     * Provide a configuration builder with the default SDL values setup.
     *
     * @return builder to set the apiSecret
     */
    public static CastleConfigurationBuilder defaultConfigBuilder() {
        CastleConfigurationBuilder builder = new CastleConfigurationBuilder();
        builder.withDefaultWhitelist();
        builder.withDefaultBlacklist();
        builder.withTimeout(500);
        builder.withDefaultFailoverStrategy();
        return builder;
    }

    public static CastleConfigurationBuilder aConfigBuilder() {
        return new CastleConfigurationBuilder();
    }

    public CastleConfigurationBuilder withDefaultWhitelist() {
        this.whiteListHeaders = new LinkedList<>();
        whiteListHeaders.add("User-Agent");
        whiteListHeaders.add("Accept-Language");
        whiteListHeaders.add("Accept-Encoding");
        whiteListHeaders.add("Accept-Charset");
        whiteListHeaders.add("Accept");
        whiteListHeaders.add("Accept-Datetime");
        whiteListHeaders.add("X-Forwarded-For");
        whiteListHeaders.add("Forwarded");
        whiteListHeaders.add("X-Forwarded");
        whiteListHeaders.add("X-Real-IP");
        whiteListHeaders.add("REMOTE_ADDR");
        return this;
    }


    public CastleConfigurationBuilder withDefaultBlacklist() {
        this.blackListHeaders = new LinkedList<>();
        this.blackListHeaders.add("Cookie");
        return this;
    }

    public CastleConfigurationBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public CastleConfigurationBuilder withFailoverStrategy(AuthenticateStrategy failoverStrategy) {
        this.failoverStrategy = failoverStrategy;
        return this;
    }


    public CastleConfigurationBuilder withDefaultFailoverStrategy() {
        return this.withFailoverStrategy(new AuthenticateStrategy(AuthenticateAction.ALLOW));
    }

    public CastleConfigurationBuilder withWhiteListHeaders(List<String> whiteListHeaders) {
        this.whiteListHeaders = whiteListHeaders;
        return this;
    }

    public CastleConfigurationBuilder withBlackListHeaders(List<String> blackListHeaders) {
        this.blackListHeaders = blackListHeaders;
        return this;
    }

    public CastleConfigurationBuilder withApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public CastleConfiguration build() {
        Preconditions.checkState(apiSecret != null, "The apiSecret for the castleSDK must be provided in the configuration");
        Preconditions.checkState(whiteListHeaders != null,"A whitelist of headers must be provided. If not sure, then use the default values provided by method withDefaultWhitelist");
        Preconditions.checkState(blackListHeaders != null,"A blacklist of headers must be provided. If not sure, then use the default values provided by method withDefaultBlacklist");
        Preconditions.checkState(failoverStrategy != null,"A failover strategy must be provided. If not sure, then use the default values provided by method withDefaultFailoverStrategy");
        return new CastleConfiguration(timeout, failoverStrategy, whiteListHeaders, blackListHeaders, apiSecret);
    }
}