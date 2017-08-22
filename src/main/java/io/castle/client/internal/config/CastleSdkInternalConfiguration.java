package io.castle.client.internal.config;

import io.castle.client.internal.backend.OkRestApiBackend;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.model.json.CastleGsonModel;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;

public class CastleSdkInternalConfiguration {

    private final RestApiFactory restApiFactory;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private CastleSdkInternalConfiguration(RestApiFactory restApiFactory, CastleGsonModel model, CastleConfiguration configuration) {
        this.restApiFactory = restApiFactory;
        this.model = model;
        this.configuration = configuration;
    }

    public static CastleSdkInternalConfiguration getInternalConfiguration() {
        CastleGsonModel modelInstance = new CastleGsonModel();
        CastleConfiguration configuration = loadConfiguration();
        RestApiFactory apiFactory = loadRestApiFactory(modelInstance, configuration);
        return new CastleSdkInternalConfiguration(apiFactory, modelInstance, configuration);
    }

    /**
     * Load the configuration from environment variables.
     *
     * @return
     */
    private static CastleConfiguration loadConfiguration() {
        //TODO implement all options
        String envApiSecret = System.getenv("CASTLE_SDK_API_SECRET");
        return CastleConfigurationBuilder.defaultConfigBuilder().withApiSecret(envApiSecret).build();
    }


    /**
     * Currently only the okHttp backend is available
     *
     * @param modelInstance GSON model instance to use
     * @return the configured RestApiFactory to make backend REST calls.
     */
    private static RestApiFactory loadRestApiFactory(final CastleGsonModel modelInstance, final CastleConfiguration configuration) {
        //TODO Temp logging of request. Should we provide logging trace funcionalities to the end users?
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final String authString = ":" + configuration.getApiSecret();
        //TODO migrate to a no dependency base64 implementation.
        final Base64 base64 = new Base64(1024);
        final String authStringBase64 = "Basic " + StringUtils.newStringUtf8(base64.encode(authString.getBytes())).trim();

        final OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header("Authorization", authStringBase64).build();
                        return chain.proceed(authenticatedRequest);
                    }
                })
                .build();
        return new RestApiFactory() {

            @Override
            public RestApi buildBackend() {
                return new OkRestApiBackend(client, modelInstance, configuration);
            }
        };
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
}
