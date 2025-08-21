package io.castle.client.internal.backend;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public class OkHttpFactory implements RestApiFactory {

    private final OkHttpClient client;
    private final CastleGsonModel modelInstance;
    private final CastleConfiguration configuration;

    public OkHttpFactory(CastleConfiguration configuration, CastleGsonModel modelInstance) {
        this.configuration = configuration;
        this.modelInstance = modelInstance;
        this.client = createOkHttpClient();
    }

    private OkHttpClient createOkHttpClient() {
        final String credential = Credentials.basic("", configuration.getApiSecret());

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(configuration.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(configuration.getMaxRequests());

        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder()
                .connectTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS)
                .dispatcher(dispatcher)
                .writeTimeout(configuration.getTimeout(), TimeUnit.MILLISECONDS);
        if (configuration.isLogHttpRequests()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // TODO provide more configurable logging features.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder = builder.addInterceptor(logging);
        }

        String httpProxyAddress = configuration.getHttpProxyAddress();
        if (httpProxyAddress != null && !httpProxyAddress.isEmpty()) {
            String[] proxyParts = httpProxyAddress.split(":");
            String proxyHost = proxyParts[0];
            int proxyPort = Integer.parseInt(proxyParts[1]);
            builder = builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }

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
                .connectionSpecs(ImmutableList.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .build();

        return client;
    }

    @Override
    public RestApi buildBackend() {
        return new OkRestApiBackend(client, modelInstance, configuration);
    }
}
