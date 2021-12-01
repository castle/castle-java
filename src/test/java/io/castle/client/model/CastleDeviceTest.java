package io.castle.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.castle.client.internal.json.CastleGsonModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleDeviceTest {

    private CastleGsonModel model = new CastleGsonModel();

    @Test
    public void jsonSerialized() {
        // Given
        CastleDevice device = new CastleDevice();
        device.setId("d_id");
        device.setName("d_name");
        device.setType("d_type");
        device.setManufacturer("d_manufacturer");
        device.setModel("d_model");

        // When
        String payloadJson = model.getGson().toJson(device);
        JsonObject convertedObject = new Gson().fromJson(payloadJson, JsonObject.class);

        // Then
        Assertions.assertThat(convertedObject.get("id").getAsString()).isEqualTo("d_id");
        Assertions.assertThat(convertedObject.get("manufacturer").getAsString()).isEqualTo("d_manufacturer");
        Assertions.assertThat(convertedObject.get("model").getAsString()).isEqualTo("d_model");
        Assertions.assertThat(convertedObject.get("name").getAsString()).isEqualTo("d_name");
        Assertions.assertThat(convertedObject.get("type").getAsString()).isEqualTo("d_type");
    }

    @Test
    public void fullBuilderJson() {
        // Given
        CastleDevice device = CastleDevice.builder()
            .id("d_id")
            .name("d_name")
            .type("d_type")
            .manufacturer("d_manufacturer")
            .model("d_model")
            .build();

        // When
        String payloadJson = model.getGson().toJson(device);
        JsonObject convertedObject = new Gson().fromJson(payloadJson, JsonObject.class);

        // Then
        Assertions.assertThat(convertedObject.get("id").getAsString()).isEqualTo("d_id");
        Assertions.assertThat(convertedObject.get("manufacturer").getAsString()).isEqualTo("d_manufacturer");
        Assertions.assertThat(convertedObject.get("model").getAsString()).isEqualTo("d_model");
        Assertions.assertThat(convertedObject.get("name").getAsString()).isEqualTo("d_name");
        Assertions.assertThat(convertedObject.get("type").getAsString()).isEqualTo("d_type");
    }
}
