package io.castle.client.http;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapperSupport {

    public static ObjectMapper objectMapper() {
        final ObjectMapper om = Holder.INSTANCE;
        configure(om);
        return om;
    }

    private static void configure(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.registerModule(customAttributeModule());
    }

    private static SimpleModule customAttributeModule() {
        final SimpleModule customAttributeModule = new SimpleModule(
            "IntercomClientModule",
            new Version(1, 0, 0, null, "", "")
        );
        return customAttributeModule;
    }

    private static class Holder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }
}
