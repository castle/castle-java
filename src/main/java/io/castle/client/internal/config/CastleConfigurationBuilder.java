package io.castle.client.internal.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateStrategy;
import io.castle.client.internal.utils.HeaderNormalizer;
import io.castle.client.model.CastleSdkConfigurationException;

import java.util.LinkedList;
import java.util.List;

public class CastleConfigurationBuilder {
    private int timeout = 500;
    private AuthenticateStrategy failoverStrategy;
    private List<String> whiteListHeaders;
    private List<String> blackListHeaders;
    private String apiSecret;
    private String castleAppId;
    private CastleBackendProvider backendProvider;

    private CastleConfigurationBuilder() {
    }

    /**
     * Provide a configuration builder with the default SDL values setup.
     *
     * @return builder to set the apiSecret
     */
    public static CastleConfigurationBuilder defaultConfigBuilder() {
        CastleConfigurationBuilder builder = new CastleConfigurationBuilder()
                .withDefaultWhitelist()
                .withDefaultBlacklist()
                .withTimeout(500)
                .withDefaultFailoverStrategy()
                .withDefaultBackendProvider();
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

    public CastleConfigurationBuilder withDefaultBackendProvider() {
        return this.withBackendProvider(CastleBackendProvider.OKHTTP);
    }

    public CastleConfigurationBuilder withWhiteListHeaders(List<String> whiteListHeaders) {
        this.whiteListHeaders = whiteListHeaders;
        return this;
    }

    public CastleConfigurationBuilder withWhiteListHeaders(String... whiteListHeaders) {
        return withWhiteListHeaders(ImmutableList.copyOf(whiteListHeaders));
    }

    public CastleConfigurationBuilder withBlackListHeaders(String... black) {
        return withBlackListHeaders(ImmutableList.copyOf(black));
    }

    public CastleConfigurationBuilder withBlackListHeaders(List<String> blackListHeaders) {
        this.blackListHeaders = blackListHeaders;
        return this;
    }

    public CastleConfigurationBuilder withApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public CastleConfiguration build() throws CastleSdkConfigurationException {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (castleAppId == null || castleAppId.isEmpty()) {
            builder.add("The castleAppId for the castleSDK must be provided in the configuration. Read documentation for further details.");
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            builder.add("The apiSecret for the castleSDK must be provided in the configuration. Read documentation for further details.");
        }
        if (whiteListHeaders == null) {
            builder.add("A whitelist of headers must be provided. If not sure, then use the default values provided by method withDefaultWhitelist. Read documentation for further details.");
        }
        if (blackListHeaders == null) {
            builder.add("A blacklist of headers must be provided. If not sure, then use the default values provided by method withDefaultBlacklist. Read documentation for further details.");
        }
        if (failoverStrategy == null) {
            builder.add("A failover strategy must be provided. If not sure, then use the default values provided by method withDefaultFailoverStrategy. Read documentation for further details.");
        }
        if (backendProvider == null) {
            builder.add("A backend provider must be selected. If not sure, then use the default values provided by method withDefaultBackendProvider. Read documentation for further details.");
        }
        ImmutableList<String> errorMessages = builder.build();
        if (!errorMessages.isEmpty()) {
            throw new CastleSdkConfigurationException(Joiner.on(System.lineSeparator()).join(errorMessages));
        }
        return new CastleConfiguration(timeout,
                failoverStrategy,
                HeaderNormalizer.normalizeList(whiteListHeaders),
                HeaderNormalizer.normalizeList(blackListHeaders),
                apiSecret,
                castleAppId,
                backendProvider);
    }

    public CastleConfigurationBuilder withCastleAppId(String castleAppId) {
        this.castleAppId = castleAppId;
        return this;
    }

    public CastleConfigurationBuilder withBackendProvider(CastleBackendProvider backendProvider) {
        this.backendProvider = backendProvider;
        return this;
    }

}