package io.castle.client.internal.utils;

import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.AuthenticateAction;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VerdictTransportModelTest {

    @Test
    public void transportProtocolIsWellDeserialized() {
        // given a json input value
        String json = "{\n" +
                                                "  \"action\": \"deny\",\n" +
                                                "  \"user_id\": \"12345\",\n" +
                                                "  \"device_token\": \"abcdefg1234\",\n" +
                                                "  \"risk_policy\": {\n" +
                                                "    \"id\": \"q-rbeMzBTdW2Fd09sbz55A\",\n" +
                                                "    \"revision_id\": \"pke4zqO2TnqVr-NHJOAHEg\",\n" +
                                                "    \"name\": \"Block Users from X\",\n" +
                                                "    \"type\": \"bot\"\n" +
                                                "  }\n" +
                                                "}";
        // and castle gson model
        CastleGsonModel model = new CastleGsonModel();


        // when the Verdict transport object is deserialized
        VerdictTransportModel loaded = model.getGson().fromJson(json, VerdictTransportModel.class);

        Assertions.assertThat(loaded.getAction()).isEqualTo(AuthenticateAction.DENY);
        Assertions.assertThat(loaded.getDeviceToken()).isEqualTo("abcdefg1234");
        Assertions.assertThat(loaded.getUserId()).isEqualTo("12345");
        Assertions.assertThat(loaded.getRiskPolicy().getId()).isEqualTo("q-rbeMzBTdW2Fd09sbz55A");
        Assertions.assertThat(loaded.getRiskPolicy().getRevisionId()).isEqualTo("pke4zqO2TnqVr-NHJOAHEg");
        Assertions.assertThat(loaded.getRiskPolicy().getName()).isEqualTo("Block Users from X");
        Assertions.assertThat(loaded.getRiskPolicy().getType()).isEqualTo("bot");
    }
}
