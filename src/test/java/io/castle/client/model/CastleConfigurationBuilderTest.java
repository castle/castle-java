package io.castle.client.model;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleConfigurationBuilderTest {

    @Test
    public void buildDefaultConfiguration() throws CastleSdkConfigurationException {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.defaultConfigBuilder();
        String apiSecret = "TestApiSecret";
        String castleAppId = "TestCastleAppId";
        builder.withApiSecret(apiSecret);
        builder.withCastleAppId(castleAppId);
        //when
        CastleConfiguration config = builder.build();
        //then the configuration match the default values
        Assertions.assertThat(config.getApiSecret()).isEqualTo(apiSecret);
        Assertions.assertThat(config.getCastleAppId()).isEqualTo(castleAppId);
        Assertions.assertThat(config.getBlackListHeaders()).contains("cookie");
        Assertions.assertThat(config.getWhiteListHeaders()).contains(
                "user-agent",
                "accept-language",
                "accept-encoding",
                "accept-charset",
                "accept",
                "accept-datetime",
                "x-forwarded-for",
                "forwarded",
                "x-forwarded",
                "x-real-ip",
                "remote-addr");
        Assertions.assertThat(config.getAuthenticateFailoverStrategy().getDefaultAction()).isEqualTo(AuthenticateAction.ALLOW);
        Assertions.assertThat(config.getTimeout()).isEqualTo(500);


    }

    @Test(expected = CastleSdkConfigurationException.class)
    public void doNotAllowToCreateAConfigurationWithoutApiSecret() throws CastleSdkConfigurationException {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.aConfigBuilder();
        //when
        builder.build();
        //then a exception is thrown
    }

    @Test(expected = CastleSdkConfigurationException.class)
    public void defaultConfigBuilderDoNotContainApiSecret() throws CastleSdkConfigurationException {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.defaultConfigBuilder();
        //when
        builder.build();
        //then a exception is thrown
    }



    @Test(expected = CastleSdkConfigurationException.class)
    public void builderWithEmptyAppIDAndAPISecretException() throws CastleSdkConfigurationException {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.aConfigBuilder();
        builder.withApiSecret("");
        builder.withCastleAppId("");

        //when
        builder.build();
        //then a exception is thrown
    }

}