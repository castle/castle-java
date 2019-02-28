package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.utils.SDKVersion;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.collect.ImmutableMap;

import javax.servlet.http.HttpServletRequest;

public class CastleIdentifyHttpTest extends AbstractCastleHttpLayerTest {

    public CastleIdentifyHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void identifyEndpointTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when and an identify request is made
        sdk.onRequest(request).identify(id);

        // then the json match specification
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void identifyEndpointWithTraitObjectTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();


        // when an identify request is made with a traits object
        sdk.onRequest(request).identify(id, ImmutableMap.builder()
                .put("x", "valueX")
                .put("y", 234567)
                .build());

        // then the json match specification
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void identifyEndpointFullOptionsTest() throws InterruptedException, JSONException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when an identify request is made with a traits object and active option
        sdk.onRequest(request).identify(id,  ImmutableMap.builder()
                .put("x", "valueX")
                .put("y", 234567)
                .build(), false);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        JSONAssert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":false,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"}," + SDKVersion.getLibraryString() +"},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8(), false);
    }

    @Test
    public void identifyEndpointFailuresAreIgnoredTest() throws InterruptedException {
        // given backend response is not 200
        server.enqueue(new MockResponse().setResponseCode(404));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when an identify request is made with a traits object
        sdk.onRequest(request).identify(id,  ImmutableMap.builder()
                .put("x", "valueX")
                .put("y", 234567)
                .build());

        // then no exceptions are throw
    }

}