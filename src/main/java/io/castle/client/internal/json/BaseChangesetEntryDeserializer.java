package io.castle.client.internal.json;

import com.google.gson.*;
import io.castle.client.model.generated.BaseChangesetEntry;
import io.castle.client.model.generated.ChangedChangesetEntry;
import io.castle.client.model.generated.ChangesetEntry;

import java.lang.reflect.Type;

public class BaseChangesetEntryDeserializer implements JsonDeserializer<BaseChangesetEntry> {
    @Override
    public BaseChangesetEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        if (object.has("changed") && !object.has("to") && !object.has("from")) {
            return new ChangesetEntry()
                    .to(object.get("to").getAsString())
                    .from(object.get("from").getAsString());
        }
        return new ChangedChangesetEntry();
    }
}
