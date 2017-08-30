package io.castle.client.internal.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.castle.client.model.AuthenticateAction;

import java.lang.reflect.Type;

public class AuthenticateActionDeserializer implements JsonDeserializer<AuthenticateAction> {
    @Override
    public AuthenticateAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return AuthenticateAction.fromAction(json.getAsString());
    }
}
