package io.castle.client.model.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import io.castle.client.model.CastleHeader;
import io.castle.client.model.CastleHeaders;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

public class CastleHeadersDeserializer implements JsonDeserializer<CastleHeaders> {
    @Override
    public CastleHeaders deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CastleHeaders headers = new CastleHeaders();
        ImmutableList.Builder<CastleHeader> builder = ImmutableList.builder();
        JsonObject root = json.getAsJsonObject();
        Set<String> keys = root.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            JsonElement jsonElement = root.get(key);
            if (jsonElement.isJsonPrimitive()) {
                CastleHeader header = new CastleHeader();
                header.setKey(key);
                header.setValue(jsonElement.getAsString());
                builder.add(header);
            }
        }
        headers.setHeaders(builder.build());
        return headers;
    }
}
