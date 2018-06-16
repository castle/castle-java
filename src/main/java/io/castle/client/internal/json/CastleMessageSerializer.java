package io.castle.client.internal.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.Gson;

import io.castle.client.internal.utils.ContextMerge;
import io.castle.client.model.CastleMessage;

import java.lang.reflect.Type;
import java.util.HashMap;

public class CastleMessageSerializer implements JsonSerializer<CastleMessage> {

    private final Gson gson = CastleGsonModel.createGsonBuilder().create();

    @Override
    public JsonElement serialize(CastleMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement root = gson.toJsonTree(message);
        HashMap other = message.getOther();
        if (other == null) {
            return root;
        }
        JsonElement otherJson = context.serialize(message.getOther());
        ContextMerge merger = new ContextMerge();
        return merger.merge(root.getAsJsonObject(), otherJson.getAsJsonObject());
    }
}
