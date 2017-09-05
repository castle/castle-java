package io.castle.client.internal.json;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleGsonModelTest {

    @Test
    public void trimLongStringValues() {
        // given a simple pojo with String value of len 3000
        TestPojo pojo = new TestPojo();
        pojo.setValue(12);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            builder.append("1234567890");
        }
        String longString = builder.toString();
        pojo.setStringValue(longString);

        // and a castle gson model
        Gson gson = new CastleGsonModel().getGson();

        // when the pojo is serialized and deserialized
        String json = gson.toJson(pojo);
        TestPojo reloaded = gson.fromJson(json, TestPojo.class);
        // then the loaded values has a len of 2048 characters.
        Assertions.assertThat(reloaded.getStringValue()).hasSize(2048);

    }


    public static class TestPojo {
        Integer value;
        String stringValue;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

}