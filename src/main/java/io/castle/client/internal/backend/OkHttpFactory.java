package io.castle.client.internal.backend;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class OkHttpFactory implements RestApiFactory {

    private final OkHttpClient client;
    private final CastleGsonModel modelInstance;
    private final CastleConfiguration configuration;

    public OkHttpFactory(CastleConfiguration configuration, CastleGsonModel modelInstance) {
        this.configuration = configuration;
        this.modelInstance = modelInstance;
        client = createOkHttpClient();
    }

    private OkHttpClient createOkHttpClient() {
        final String credential = Credentials.basic("", configuration.getApiSecret());

        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder()
                .connectTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS);
        if (configuration.isLogHttpRequests()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // TODO provide more configurable logging features.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder = builder.addInterceptor(logging);
        }

        ConnectionSpec sslSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_1, TlsVersion.TLS_1_2, TlsVersion.TLS_1_3, TlsVersion.SSL_3_0)
                .build();

        ConnectionSpec cleartetSpec = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT)
                .build();

        OkHttpClient client = builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header("Authorization", credential).build();
                        return chain.proceed(authenticatedRequest);
                    }
                })
                .connectionSpecs(ImmutableList.of(sslSpec, cleartetSpec))
                .build();

        return client;
    }

    @Override
    public RestApi buildBackend() {
        return new OkRestApiBackend(client, modelInstance, configuration);
    }
}
