package io.castle.client.model;

import com.google.common.collect.ImmutableMap;
import io.castle.client.internal.json.CastleGsonModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleMessageTest {

    private CastleGsonModel model = new CastleGsonModel();

    @Test
    public void jsonSerialized() {
        // Given
        CastleMessage message = new CastleMessage("event");

        // When
        String payloadJson = model.getGson().toJson(message);

        // Then
        Assertions.assertThat(payloadJson).isEqualTo("{\"event\":\"event\"}");
    }

    @Test
    public void fullBuilderJson() {
        // Given
        CastleMessage message = CastleMessage.builder("event")
            .createdAt("2018-01-01")
            .deviceToken("1234")
            .reviewId("2345")
            .properties(ImmutableMap.builder()
                .put("key", "val")
                .build())
            .userId("3456")
            .userTraits(ImmutableMap.builder()
                .put("key", "val")
                .build())
            .build();

        // When
        String payloadJson = model.getGson().toJson(message);

        // Then
        Assertions.assertThat(payloadJson).isEqualTo("{\"timestamp\":\"2018-01-01\",\"device_token\":\"1234\",\"event\":\"event\",\"properties\":{\"key\":\"val\"},\"review_id\":\"2345\",\"user_id\":\"3456\",\"user_traits\":{\"key\":\"val\"}}");
    }

    @Test
    public void otherProperties() {
        CastleMessage message = CastleMessage.builder("event")
                .put("key", "value")
                .build();

        String payloadJson = model.getGson().toJson(message);

        Assertions.assertThat(payloadJson).isEqualTo("{\"event\":\"event\",\"key\":\"value\"}");
    }
}
