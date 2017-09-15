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
                "  \"user_id\": \"1234567\"\n" +
                "}";
        // and castle gson model
        CastleGsonModel model = new CastleGsonModel();

        // when the Verdict transport object is deserialized
        VerdictTransportModel loaded = model.getGson().fromJson(json, VerdictTransportModel.class);

        // then the instance match the provided values
        VerdictTransportModel expected = new VerdictTransportModel();
        expected.setAction(AuthenticateAction.DENY);
        expected.setUserId("1234567");
        Assertions.assertThat(loaded).isEqualToComparingFieldByField(expected);

    }

}