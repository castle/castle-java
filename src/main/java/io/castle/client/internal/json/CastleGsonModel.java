package io.castle.client.internal.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.CastleHeaders;
import io.castle.client.model.CastleMessage;

public class CastleGsonModel {

    private final Gson gson;

    public CastleGsonModel() {
        GsonBuilder builder = createGsonBuilder();
        builder.registerTypeAdapter(CastleHeaders.class, new CastleHeadersSerializer());
        builder.registerTypeAdapter(String.class, new StringJsonSerializer());
        builder.registerTypeAdapter(CastleMessage.class, new CastleMessageSerializer());
        builder.registerTypeAdapter(CastleHeaders.class, new CastleHeadersDeserializer());
        builder.registerTypeAdapter(AuthenticateAction.class, new AuthenticateActionDeserializer());
        this.gson = builder.create();
    }

    public Gson getGson() {
        return gson;
    }

    public static GsonBuilder createGsonBuilder() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }
}
