package io.castle.client;

import io.castle.client.internal.backend.OkHttpFactory;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCastleHttpLayerTest {

    private final AuthenticateFailoverStrategy testAuthenticateFailoverStrategy;

    Castle sdk;
    MockWebServer server;
    HttpUrl testServerBaseUrl;

    protected AbstractCastleHttpLayerTest(AuthenticateFailoverStrategy testAuthenticateFailoverStrategy) {
        this.testAuthenticateFailoverStrategy = testAuthenticateFailoverStrategy;
    }

    @Before
    public void prepare() throws NoSuchFieldException, IllegalAccessException, CastleSdkConfigurationException, IOException {
        //Given a mocked API server
        server = new MockWebServer();
        server.start(InetAddress.getByName("127.0.0.1"),0);
        //Given a SDK instance
        sdk = new Castle(CastleSdkInternalConfiguration.getInternalConfiguration());
        CastleConfiguration configuration = sdk.getInternalConfiguration().getConfiguration();
        testServerBaseUrl = server.url("/");
        CastleConfiguration mockedApiConfiguration = CastleConfigurationBuilder.aConfigBuilder()
                .withApiSecret(configuration.getApiSecret())
                .withApiBaseUrl(testServerBaseUrl.toString())
                .withLogHttpRequests(true)
                .withDenyListHeaders(configuration.getDenyListHeaders())
                .withAllowListHeaders(configuration.getAllowListHeaders())
                .withCastleAppId(configuration.getCastleAppId())
                .withBackendProvider(configuration.getBackendProvider())
                .withAuthenticateFailoverStrategy(testAuthenticateFailoverStrategy)
                .withTimeout(configuration.getTimeout())
                .build();

        sdk = Castle.initialize(mockedApiConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }


    protected <T> void waitForValueAndVerify(AtomicReference<T> result, T expected) {
        T extracted = waitForValue(result);
        Assertions.assertThat(extracted).usingRecursiveComparison().isEqualTo(expected);
    }

    protected <T> T waitForValue(AtomicReference<T> result) {
        T value = result.get();
        int maxNrOfSleeps = 10;
        while (value == null && maxNrOfSleeps > 0) {
            maxNrOfSleeps = maxNrOfSleeps - 1;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            value = result.get();
        }
        Assertions.assertThat(value).isNotNull();
        return value;
    }
}
