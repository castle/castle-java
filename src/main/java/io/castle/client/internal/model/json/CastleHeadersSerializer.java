package io.castle.client.internal.model.json;

import com.google.gson.*;
import io.castle.client.internal.model.CastleHeader;
import io.castle.client.internal.model.CastleHeaders;

import java.lang.reflect.Type;
import java.util.Iterator;

public class CastleHeadersSerializer implements JsonSerializer<CastleHeaders> {
    @Override
    public JsonElement serialize(CastleHeaders headers, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        for (Iterator<CastleHeader> iterator = headers.getHeaders().iterator(); iterator.hasNext(); ) {
            CastleHeader header = iterator.next();
            String key = header.getKey();
            String value = header.getValue();
            root.add(key, new JsonPrimitive(value));
        }
        return root;
    }
}
