package io.castle.client;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.CastleSdkConfigurationException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.InetAddress;

public abstract class AbstractCastleHttpLayerTest {

    Castle sdk;
    MockWebServer server;
    HttpUrl testServerBaseUrl;

    @Before
    public void prepare() throws NoSuchFieldException, IllegalAccessException, CastleSdkConfigurationException, IOException {
        //Given a mocked API server
        server = new MockWebServer();
        server.start(InetAddress.getByName("127.0.0.1"),0);
        //Given a SDK instance
        Castle loaded = new Castle(CastleSdkInternalConfiguration.getInternalConfiguration());
        CastleConfiguration configuration = loaded.getInternalConfiguration().getConfiguration();
        testServerBaseUrl = server.url("/");
        CastleConfiguration mockedApiConfiguration = CastleConfigurationBuilder.aConfigBuilder()
                .withApiSecret(configuration.getApiSecret())
                .withApiBaseUrl(testServerBaseUrl.toString())
                .withLogHttpRequests(true)
                .withDenyListHeaders(configuration.getDenyListHeaders())
                .withAllowListHeaders(configuration.getAllowListHeaders())
                .withCastleAppId(configuration.getCastleAppId())
                .withBackendProvider(configuration.getBackendProvider())
                .withTimeout(configuration.getTimeout())
                .build();
        loaded.close();

        sdk = Castle.initialize(mockedApiConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        if (sdk != null) {
            sdk.close();
        }
        server.shutdown();
    }
}
