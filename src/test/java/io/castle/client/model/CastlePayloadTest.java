package io.castle.client.model;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.json.CastleGsonModel;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Comparator;
import java.util.List;

public class CastlePayloadTest {

    private CastleGsonModel model = new CastleGsonModel();

    @Test
    public void minimalContextAsJson() {

        //given
        CastlePayload aOptions = new CastlePayload();

        //when
        String optionsJson = model.getGson().toJson(aOptions);

        //Then
        Assertions.assertThat(optionsJson).isEqualTo("{}");

    }

    @Test
    public void booleanContextValues() throws JSONException {
        //given
        CastlePayload aOptions = new CastlePayload();
        aOptions.setFingerprint(true);

        //when
        String contextJson = model.getGson().toJson(aOptions);

        //Then generated json match the api contract
        String expectedJson = "{\"fingerprint\":true}";

        JSONAssert.assertEquals(expectedJson, contextJson, true);
    }

    @Test
    public void completeContextJsonSpec() throws JSONException {

        //given
        CastlePayload aOptions = new CastlePayload();
        aOptions.setFingerprint("fingerprintX");

        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(ImmutableList.of(
                new CastleHeader("key1", "value1"),
                new CastleHeader("key2", "value2")
        ));
        aOptions.setHeaders(headers);

        aOptions.setIp("ip");

        //when
        String contextJson = model.getGson().toJson(aOptions);

        //Then generated json match the api contract
        String expectedJson = "{\"fingerprint\":\"fingerprintX\"," +
                "\"headers\":{\"key1\":\"value1\",\"key2\":\"value2\"}," +
                "\"ip\":\"ip\"}";
        JSONAssert.assertEquals(expectedJson, contextJson, true);

        // And json to object create the same structure
        CastlePayload fromJson = model.getGson().fromJson(expectedJson, CastlePayload.class);
        Assertions.assertThat(fromJson).isEqualToComparingFieldByFieldRecursively(aOptions);
    }

    @Test
    public void notMatchingHeadersJsonElementsAreIgnored() {

        //Given a json with headers not matching the api contract
        String notMatchingJson = "{\"fingerprint\":\"fingerprintX\"," +
                "\"headers\":{\"key1\":\"value1\",\"key2\":\"value2\"," +
                "\"badKey1\":[\"v1\",\"v2\"],\"badKey2\":{\"nested\":\"value\"}" + // Invalid headers fields
                "}," +
                "\"ip\":\"ip\"}";

        //When CastlePayload is created
        CastlePayload created = model.getGson().fromJson(notMatchingJson, CastlePayload.class);

        //Then the bad headers are ignored
        List<CastleHeader> headers = created.getHeaders().getHeaders();
        Assertions.assertThat(headers)
                .usingElementComparator(new Comparator<CastleHeader>() {
                    @Override
                    public int compare(CastleHeader castleHeader, CastleHeader that) {
                        String key = castleHeader.getKey();
                        String value = castleHeader.getValue();
                        if (key != null ? !key.equals(that.getKey()) : that.getKey() != null) {
                            return -1;
                        }
                        if (value != null ? !value.equals(that.getValue()) : that.getValue() != null) {
                            return -1;
                        }
                        return 0;
                    }
                })
                .containsExactlyInAnyOrder(new CastleHeader("key1", "value1"), new CastleHeader("key2", "value2"));

    }

    @Test
    public void toStringMethodCreatesAWellFormedStringFromAnEmptyOptionsInstance() {

        // given
        String expected = "CastlePayload{fingerprint='null', headers=null, ip='null'}";
        CastlePayload options = new CastlePayload();

        //when
        String toString = options.toString();
        System.out.println(toString);
        Assert.assertEquals(expected, toString);

    }

    @Test
    public void toStringMethodCreatesAWellFormedStringFromAFullExample() throws JSONException {
        //given
        CastlePayload aOptions = new CastlePayload();

        aOptions.setFingerprint("fingerprintX");

        aOptions.setIp("ip");


        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(ImmutableList.of(
                new CastleHeader("key1", "value1"),
                new CastleHeader("key2", "value2")
        ));
        aOptions.setHeaders(headers);

        String expected = "CastlePayload{fingerprint='fingerprintX', " +
                "headers=CastleHeaders{headers=[CastleHeader{key='key1', value='value1'}, CastleHeader{key='key2', value='value2'}]}, " +
                "ip='ip'" +
                "}";


        //when
        String toString = aOptions.toString();

        //then a String should be generated in the usual format (i.e., in the format generated automatically by the IDE.)
        Assert.assertEquals(expected, toString);

    }
}