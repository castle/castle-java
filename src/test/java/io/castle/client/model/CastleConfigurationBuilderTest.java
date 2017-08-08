package io.castle.client.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleConfigurationBuilderTest {

    @Test
    public void buildDefaultConfiguration() {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.defaultConfigBuilder();
        String apiSecret = "TestApiSecret";
        builder.withApiSecret(apiSecret);
        //when
        CastleConfiguration config = builder.build();
        //then the configuration match the default values
        Assertions.assertThat(config.getApiSecret()).isEqualTo(apiSecret);
        Assertions.assertThat(config.getBlackListHeaders()).contains("Cookie");
        Assertions.assertThat(config.getWhiteListHeaders()).contains(
                "User-Agent",
                "Accept-Language",
                "Accept-Encoding",
                "Accept-Charset",
                "Accept",
                "Accept-Datetime",
                "X-Forwarded-For",
                "Forwarded",
                "X-Forwarded",
                "X-Real-IP",
                "REMOTE_ADDR");
        Assertions.assertThat(config.getFailoverStrategy().getDefaultAction()).isEqualTo(AuthenticateAction.ALLOW);
        Assertions.assertThat(config.getTimeout()).isEqualTo(500);


    }

    @Test(expected = IllegalStateException.class)
    public void doNotAllowToCreateAConfigurationWithoutApiSecret() {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.aConfigBuilder();
        //when
        builder.build();
        //then a exception is throw
    }

    @Test(expected = IllegalStateException.class)
    public void defaultConfigBuilderDoNotContaintApiSecret() {
        //given
        CastleConfigurationBuilder builder = CastleConfigurationBuilder.defaultConfigBuilder();
        //when
        builder.build();
        //then a exception is throw
    }

}