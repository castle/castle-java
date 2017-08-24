package io.castle.client.internal.config;

import com.google.common.io.BaseEncoding;
import io.castle.client.internal.backend.OkRestApiBackend;
import io.castle.client.internal.backend.RestApi;
import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.CastleSdkConfigurationException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CastleSdkInternalConfiguration {

    private final RestApiFactory restApiFactory;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private CastleSdkInternalConfiguration(RestApiFactory restApiFactory, CastleGsonModel model, CastleConfiguration configuration) {
        this.restApiFactory = restApiFactory;
        this.model = model;
        this.configuration = configuration;
    }

    public static CastleSdkInternalConfiguration getInternalConfiguration() throws CastleSdkConfigurationException {
        CastleGsonModel modelInstance = new CastleGsonModel();
        CastleConfiguration configuration = new ConfigurationLoader().loadConfiguration();
        RestApiFactory apiFactory = loadRestApiFactory(modelInstance, configuration);
        return new CastleSdkInternalConfiguration(apiFactory, modelInstance, configuration);
    }

    /**
     * Currently only the okHttp backend is available
     *
     * @param modelInstance GSON model instance to use.
     * @param configuration CastleConfiguration instance. TODO!!!!!
     * @return The configured RestApiFactory to make backend REST calls.
     */
    private static RestApiFactory loadRestApiFactory(final CastleGsonModel modelInstance, final CastleConfiguration configuration) {
        //TODO Temp logging of request. Should we provide logging trace functionalities to the end users?
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final String authString = ":" + configuration.getApiSecret();
        final String authStringBase64 = "Basic " + BaseEncoding.base64().encode(authString.getBytes());

        final OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS)
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
