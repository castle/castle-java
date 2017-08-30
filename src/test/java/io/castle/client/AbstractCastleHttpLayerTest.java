package io.castle.client;

import io.castle.client.internal.backend.OkHttpFactory;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCastleHttpLayerTest {

    private final AuthenticateFailoverStrategy testAuthenticateFailoverStrategy;

    Castle sdk;
    MockWebServer server;

    protected AbstractCastleHttpLayerTest(AuthenticateFailoverStrategy testAuthenticateFailoverStrategy) {
        this.testAuthenticateFailoverStrategy = testAuthenticateFailoverStrategy;
    }

    @Before
    public void prepare() throws NoSuchFieldException, IllegalAccessException, CastleSdkConfigurationException, IOException {
        //Given a mocked API server
        server = new MockWebServer();
        server.start();
        //Given a SDK instance
        sdk = new Castle(CastleSdkInternalConfiguration.getInternalConfiguration());
        CastleConfiguration configuration = sdk.getInternalConfiguration().getConfiguration();
        CastleConfiguration mockedApiConfiguration = CastleConfigurationBuilder.aConfigBuilder()
                .withApiSecret(configuration.getApiSecret())
                .withApiBaseUrl(server.url("/").toString())
                .withBlackListHeaders(configuration.getBlackListHeaders())
                .withWhiteListHeaders(configuration.getWhiteListHeaders())
                .withCastleAppId(configuration.getCastleAppId())
                .withBackendProvider(configuration.getBackendProvider())
                .withAuthenticateFailoverStrategy(testAuthenticateFailoverStrategy)
                .withTimeout(configuration.getTimeout())
                .build();
        OkHttpFactory mockedFactory = new OkHttpFactory(mockedApiConfiguration, sdk.getInternalConfiguration().getModel());

        //When the utils are used to override the internal backend factory
        SdkMockUtil.modifyInternalBackendFactory(sdk, mockedFactory);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }


    protected <T> void waitForValueAndVerify(AtomicReference<T> result, T expected) {
        T extracted = waitForValue(result);
        Assertions.assertThat(extracted).isEqualToComparingFieldByField(expected);
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


    public static class CustomAppProperties {
        private String a;
        private int b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static class CustomAppIdentifyTrait {
        private String x;
        private int y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }


}
