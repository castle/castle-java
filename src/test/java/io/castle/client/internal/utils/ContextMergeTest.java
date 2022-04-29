package io.castle.client.internal.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ContextMergeTest {

    @Test
    public void mergeIndependentJsonTrees() throws JSONException {
        String targetJson = "{\"a\":10}";
        String sourceJson = "{\"b\":20}";
        String expectedResult = "{\"a\":10,\"b\":20}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void mergeIndependentDeeplyJsonTrees() throws JSONException {
        String targetJson = "{\"x\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String sourceJson = "{\"b\":20, \"x\":{\"y\":{\"z\":{\"merge\":\"toAddValue\"}}}}";
        String expectedResult = "{\"b\":20,\"x\":{\"y\":{\"z\":{\"a\":\"test\",\"merge\":\"toAddValue\"}}}}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void nullOnSourceMustDeleteProperty() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":\"targetValue\"}";
        String sourceJson = "{\"b\":20,\"test\":null}";
        String expectedResult = "{\"a\":10,\"b\":20}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceReplaceTargetPrimitiveCase() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":\"targetValue\"}";
        String sourceJson = "{\"b\":20,\"test\":\"sourceValue\"}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":\"sourceValue\"}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceAndTargetMergeOnObjectCase() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":{\"x\":12}}";
        String sourceJson = "{\"b\":20,\"test\":{\"y\":34}}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":{\"x\":12,\"y\":34}}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void sourceAndTargetAddArrays() throws JSONException {
        String targetJson = "{\"a\":10,\"test\":[\"t1\",\"t2\"]}";
        String sourceJson = "{\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":[\"t1\",\"t2\",\"s1\",\"s2\"]}";
        runTestCase(targetJson, sourceJson, expectedResult);
    }

    @Test
    public void baseWithArrayAdditionWithJsonObject() throws JSONException {
        String additionJson = "{\"a\":10,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String baseJson = "{\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        String expectedResult = "{\"b\":20,\"a\":10,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test
    public void baseWithPrimitiveAdditionWithJsonObject() throws JSONException {
        String additionJson = "{\"a\":10,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String baseJson = "{\"b\":20,\"test\":10}";
        String expectedResult = "{\"b\":20,\"a\":10,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test(expected = JsonSyntaxException.class)
    public void baseWithInvalidJsonAdditionWithJsonObject() throws JSONException {
        String baseJson = "{\"a\":10,\"test\":{\"y\":{\"z\":{\"a\"\"test\"}}}}";
        String additionJson = "{\"b\":20,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String expectedResult = "exception, since base JSON is invalid";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test
    public void baseWithJsonObjectAdditionWithArray() throws JSONException {
        String baseJson = "{\"a\":10,\"test\":{\"y\":{\"z\":{\"a\":\"test\"}}}}";
        String additionJson = "{\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test
    public void baseWithArrayAdditionWithArray() throws JSONException {
        String baseJson = "{\"a\":10,\"test\":[\"s1\",\"s3\"]}";
        String additionJson = "{\"b\":20,\"test\":[\"s2\",\"s4\"]}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":[\"s1\",\"s3\",\"s2\",\"s4\"]}";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test
    public void baseWithPrimitiveAdditionWithArray() throws JSONException {
        String baseJson = "{\"a\":10,\"test\":11}";
        String additionJson = "{\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":[\"s1\",\"s2\"]}";
        runTestCase(baseJson, additionJson, expectedResult);
    }


    @Test
    public void baseAndAdditionArraysWithJsonObjectElements() throws JSONException {
        String baseJson = "{\"a\":10,\"test\":[{\"y\":{\"z\":{\"a\":\"test\"}}},\"s2\"]}";
        String additionJson = "{\"b\":20,\"test\":[\"s1\",[\"s3\",\"s4\"]]}";
        String expectedResult = "{\"a\":10,\"b\":20,\"test\":[{\"y\":{\"z\":{\"a\":\"test\"}}},\"s2\",\"s1\",[\"s3\",\"s4\"]]}";
        runTestCase(baseJson, additionJson, expectedResult);
    }

    @Test
    public void baseAndAdditionWithBooleanValues() throws JSONException {
        String baseJson = "{\"a\":true}";
        String additionJson = "{\"b\":false}";
        String expectedResult = "{\"a\":true,\"b\":false}";
        runTestCase(baseJson, additionJson, expectedResult);
    }


    private void runTestCase(String targetJson, String sourceJson, String expectedResult) throws JSONException {
        //Given
        ContextMerge contextMerge = new ContextMerge();

        JsonObject target = JsonParser.parseString(targetJson).getAsJsonObject();
        JsonObject source = JsonParser.parseString(sourceJson).getAsJsonObject();

        //When
        String mergeJson = contextMerge.merge(target, source).toString();
        //Then all the properties are copied
        String expectedJson;
        expectedJson = expectedResult;
        Assertions.assertThat(mergeJson).isEqualTo(expectedJson);
        JSONAssert.assertEquals(expectedJson, mergeJson, true);
    }


}