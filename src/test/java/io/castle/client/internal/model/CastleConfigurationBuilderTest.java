package io.castle.client.internal.model;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
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