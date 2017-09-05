package io.castle.client.internal.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StringJsonSerializerTest {

    @Test
    public void nullStringSerializationIsSafe() {
        // given
        StringJsonSerializer serializer = new StringJsonSerializer();

        // when
        JsonElement jsonElement = serializer.serialize(null, null, null);

        // then response is null json
        Assertions.assertThat(jsonElement).isEqualTo(JsonNull.INSTANCE);
    }

}