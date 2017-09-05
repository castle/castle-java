package io.castle.client.internal.utils;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ContextMerge {
    /**
     * Merges the base and the addition JSON tree.
     *
     * The base value is not mutated during the process.
     * References to the addition tree elements may be used on the resulting tree.
     *
     * @param base     JsonObject whose elements will be modified.
     * @param addition JsonObject containing the elements that will be updated in the base.
     * @return A JsonObject whose elements are the result of taking a copy of the base and updating them according to the elements of the addition JsonObject.
     */
    public JsonObject merge(JsonObject base, JsonObject addition) {
        if (addition==null){
            return new JsonObject();
        }
        return mergeDeep(base, addition);
    }

    private JsonObject mergeDeep(JsonObject base, JsonObject addition) {
        Set<String> baseProperties = base.keySet();
        Set<String> additionProperties = addition.keySet();

        Set<String> onlyOnBase = Sets.difference(baseProperties, additionProperties);
        Set<String> onlyOnAddition = Sets.difference(additionProperties, baseProperties);
        Set<String> propertiesToMerge = Sets.intersection(baseProperties, additionProperties);

        JsonObject result = new JsonObject();

        for (Iterator<String> iterator = onlyOnBase.iterator(); iterator.hasNext(); ) {
            String baseProp = iterator.next();
            addEntryToResult(result, baseProp, base.get(baseProp));
        }
        for (Iterator<String> iterator = onlyOnAddition.iterator(); iterator.hasNext(); ) {
            String additionProp = iterator.next();
            addEntryToResult(result, additionProp, addition.get(additionProp));
        }
        for (Iterator<String> iterator = propertiesToMerge.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            JsonElement additionValue = addition.get(key);
            JsonElement baseValue = base.get(key);
            if (additionValue.isJsonNull()) {
                addEntryToResult(result, key, additionValue);
            } else {
                if (additionValue.isJsonObject()) {
                    if (baseValue.isJsonObject()) {
                        addEntryToResult(result, key, mergeDeep(baseValue.getAsJsonObject(), additionValue.getAsJsonObject()));
                    } else if (baseValue.isJsonArray()) {
                        addEntryToResult(result, key, additionValue);
                        //if jsonElement.isJsonPrimitive()
                    } else {
                        addEntryToResult(result, key, additionValue);
                    }
                } else if (additionValue.isJsonArray()) {
                    if (baseValue.isJsonObject()) {
                        addEntryToResult(result, key, additionValue);
                    } else if (baseValue.isJsonArray()) {
                        addEntryToResult(result, key, mergeDeep(baseValue.getAsJsonArray(), additionValue.getAsJsonArray()));
                        //if jsonElement.isJsonPrimitive()
                    } else {
                        addEntryToResult(result, key, additionValue);
                    }
                    //if jsonElement.isJsonPrimitive()
                } else {
                    addEntryToResult(result, key, additionValue);
                }
            }
        }
        return result;
    }

    private void addEntryToResult(JsonObject result, String key, JsonElement value) {
        if (value.isJsonObject()) {
            result.add(key, deepCopy(value.getAsJsonObject()));
        } else if (value.isJsonArray()) {
            result.add(key, deepCopy(value.getAsJsonArray()));
        } else if (value.isJsonPrimitive()) {
            result.add(key, value.getAsJsonPrimitive());
        }
    }

    private JsonArray mergeDeep(JsonArray target, JsonArray source) {
        JsonArray result = deepCopy(target);
        for (JsonElement fromSource : source) {
            result.add(fromSource);
        }
        return result;
    }

    private JsonObject deepCopy(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            addEntryToResult(result, entry.getKey(), entry.getValue());
        }
        return result;
    }

    private JsonArray deepCopy(JsonArray jsonArray) {
        JsonArray result = new JsonArray();

        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
                result.add(deepCopy(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                result.add(deepCopy(jsonElement.getAsJsonArray()));
                //if jsonElement.isJsonPrimitive()
            } else {
                result.add(new JsonPrimitive(jsonElement.getAsString()));
            }
        }
        return result;
    }
}
