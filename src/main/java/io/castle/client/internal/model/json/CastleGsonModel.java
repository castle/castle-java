package io.castle.client.internal.model.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.castle.client.internal.model.CastleHeaders;

public class CastleGsonModel {

    private final Gson gson;

    public CastleGsonModel() {
        GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        builder.registerTypeAdapter(CastleHeaders.class,new CastleHeadersSerializer());
        builder.registerTypeAdapter(CastleHeaders.class,new CastleHeadersDeserializer());
        this.gson = builder.create();
    }


    public Gson getGson() {
        return gson;
    }
}
