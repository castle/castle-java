package io.castle.client.internal.json;

import com.google.common.collect.ImmutableMap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.model.CastleMessage;

import java.lang.reflect.Type;

public class CastleMessageSerializer implements JsonSerializer<CastleMessage> {

    private final Gson gson = CastleGsonModel.createGsonBuilder().create();

    @Override
    public JsonElement serialize(CastleMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement root = gson.toJsonTree(message);
        ImmutableMap other = message.getOther();
        if (other == null) {
            return root;
        }
        JsonElement otherJson = context.serialize(message.getOther());
        ContextMerge merger = new ContextMerge();
        return merger.merge(root.getAsJsonObject(), otherJson.getAsJsonObject());
    }
}
