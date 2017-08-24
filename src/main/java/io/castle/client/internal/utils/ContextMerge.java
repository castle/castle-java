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
     * Merge the base and the addition json tree.
     * The base value is not mutated during the process. References to the addition tree elements may be used on the resulting tree.
     * @param base JsonObject whose elements will be modified.
     * @param addition JsonObject containing the elements that will be updated in the base.
     * @return A JsonObject whose elements are the result of taking a copy of the base and updating them according to the elements of the addition JsonObject.
     */
    public JsonObject merge(JsonObject base, JsonObject addition) {
        return mergeDeep(base, addition);
    }

    private JsonObject mergeDeep(JsonObject target, JsonObject source) {
        Set<String> targetProperties = target.keySet();
        Set<String> sourceProperties = source.keySet();

        Set<String> onlyOnTarget = Sets.difference(targetProperties, sourceProperties);
        Set<String> onlyOnSource = Sets.difference(sourceProperties, targetProperties);
        Set<String> propertiesToMerge = Sets.intersection(targetProperties, sourceProperties);

        JsonObject result = new JsonObject();

        for (Iterator<String> iterator = onlyOnTarget.iterator(); iterator.hasNext(); ) {
            String targetProp = iterator.next();
            addEntryToResult(result, targetProp, target.get(targetProp));
        }
        for (Iterator<String> iterator = onlyOnSource.iterator(); iterator.hasNext(); ) {
            String sourceProp = iterator.next();
            addEntryToResult(result, sourceProp, source.get(sourceProp));
        }
        for (Iterator<String> iterator = propertiesToMerge.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            JsonElement sourceValue = source.get(key);
            JsonElement targetValue = target.get(key);
            if (sourceValue.isJsonNull()) {
                addEntryToResult(result, key, sourceValue);
            } else {
                if (sourceValue.isJsonObject()) {
                    if (targetValue.isJsonObject()) {
                        addEntryToResult(result, key, mergeDeep(targetValue.getAsJsonObject(), sourceValue.getAsJsonObject()));
                    } else if (targetValue.isJsonArray()) {
                        addEntryToResult(result, key, sourceValue);
                    } else if (targetValue.isJsonPrimitive()) {
                        addEntryToResult(result, key, sourceValue);
                    }
                } else if (sourceValue.isJsonArray()) {
                    if (targetValue.isJsonObject()) {
                        addEntryToResult(result, key, sourceValue);
                    } else if (targetValue.isJsonArray()) {
                        addEntryToResult(result, key, mergeDeep(targetValue.getAsJsonArray(), sourceValue.getAsJsonArray()));
                    } else if (targetValue.isJsonPrimitive()) {
                        addEntryToResult(result, key, sourceValue);
                    }
                } else if (sourceValue.isJsonPrimitive()) {
                    addEntryToResult(result, key, sourceValue);
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
            result.add(key, new JsonPrimitive(value.getAsString()));
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
            } else if (jsonElement.isJsonPrimitive()) {
                result.add(new JsonPrimitive(jsonElement.getAsString()));
            }
        }
        return result;
    }
}
