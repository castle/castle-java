package io.castle.client.internal.json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class StringJsonSerializer implements JsonSerializer<String> {

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        if( src.length() > 2048) {
            return new JsonPrimitive(src.substring(0,2048));
        }
        return new JsonPrimitive(src);
    }
}
