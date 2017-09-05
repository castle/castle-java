package io.castle.client.internal.config;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.castle.client.internal.backend.OkHttpFactory;
import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.CastleSdkConfigurationException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CastleSdkInternalConfiguration {

    private final RestApiFactory restApiFactory;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private final SecretKey sha256Key;

    private CastleSdkInternalConfiguration(RestApiFactory restApiFactory, CastleGsonModel model, CastleConfiguration configuration) {
        this.restApiFactory = restApiFactory;
        this.model = model;
        this.configuration = configuration;
        this.sha256Key = new SecretKeySpec(configuration.getApiSecret().getBytes(Charsets.UTF_8), "HmacSHA256");
    }

    public static CastleSdkInternalConfiguration getInternalConfiguration() throws CastleSdkConfigurationException {
        CastleGsonModel modelInstance = new CastleGsonModel();
        CastleConfiguration configuration = new ConfigurationLoader().loadConfiguration();
        RestApiFactory apiFactory = loadRestApiFactory(modelInstance, configuration);
        return new CastleSdkInternalConfiguration(apiFactory, modelInstance, configuration);
    }

    /**
     * Currently only the okHttp backend is available.
     *
     * @param modelInstance GSON model instance to use.
     * @param configuration CastleConfiguration instance.
     * @return The configured RestApiFactory to make backend REST calls.
     */
    private static RestApiFactory loadRestApiFactory(final CastleGsonModel modelInstance, final CastleConfiguration configuration) {
        return new OkHttpFactory(configuration, modelInstance);
    }


    public CastleGsonModel getModel() {
        return model;
    }

    public RestApiFactory getRestApiFactory() {
        return restApiFactory;
    }

    public CastleConfiguration getConfiguration() {
        return configuration;
    }

    public HashFunction getSecureHashFunction() {
        return Hashing.hmacSha256(sha256Key);
    }
}
