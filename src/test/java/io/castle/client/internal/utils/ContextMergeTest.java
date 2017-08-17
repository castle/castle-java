package io.castle.client.internal.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ContextMergeTest {

    @Test
    public void mergeIndependentJsonTrees() throws JSONException {
        String targetJson = "{\"a\":10}";
        String sourceJson = "{\"b\":20}";
        String expectedResult = "{\"a\":\"10\",\"b\":\"20\"}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void mergeIndependentDeeplyJsonTrees() throws JSONException {
        String targetJson = "{\"x\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String sourceJson = "{\"b\":20, \"x\":{\"y\":{\"z\":{\"merge\":\"toAddValue\"}}}}";
        String expectedResult = "{\"b\":\"20\",\"x\":{\"y\":{\"z\":{\"a\":\"test\",\"merge\":\"toAddValue\"}}}}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void nullOnSourceMustDeleteProperty() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":\"targetValue\"}";
        String sourceJson = "{\"b\":20,\"test\":null}";
        String expectedResult = "{\"a\":\"10\",\"b\":\"20\"}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceReplaceTargetPrimitiveCase() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":\"targetValue\"}";
        String sourceJson = "{\"b\":20,\"test\":\"sourceValue\"}";
        String expectedResult = "{\"a\":\"10\",\"b\":\"20\",\"test\":\"sourceValue\"}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceAndTargetMergeOnObjectCase() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":{\"x\":12}}";
        String sourceJson = "{\"b\":20,\"test\":{\"y\":34}}";
        String expectedResult = "{\"a\":\"10\",\"b\":\"20\",\"test\":{\"x\":\"12\",\"y\":\"34\"}}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceAndTargetAddArrays() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":[\"t1\",\"t2\"]}";
        String sourceJson = "{\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        String expectedResult = "{\"a\":\"10\",\"b\":\"20\",\"test\":[\"t1\",\"t2\",\"s1\",\"s2\"]}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    private void runTestCase(String targetJson, String sourceJson, String expectedResult) throws JSONException {
        //Given
        JsonParser jsonParser = new JsonParser();
        ContextMerge contextMerge = new ContextMerge();

        JsonObject target = jsonParser.parse(targetJson).getAsJsonObject();
        JsonObject source = jsonParser.parse(sourceJson).getAsJsonObject();

        //When
        String mergeJson = contextMerge.merge(target, source).toString();
        //Then all the properties are copied
        String expectedJson;
        expectedJson = expectedResult;
        Assertions.assertThat(mergeJson).isEqualTo(expectedJson);
        JSONAssert.assertEquals(expectedJson, mergeJson, true);
    }


}