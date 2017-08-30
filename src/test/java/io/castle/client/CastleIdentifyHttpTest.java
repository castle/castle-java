package io.castle.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class CastleIdentifyHttpTest extends AbstractCastleHttpLayerTest {

    @Test
    public void identifyEndpointTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and
        CustomAppIdentifyTrait trait = new CustomAppIdentifyTrait();
        trait.setX("valueX");
        trait.setY(234567);

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, trait);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"active\":true,\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void identifyEndpointFailuresAreIgnoredTest() throws InterruptedException {
        // given
        server.enqueue(new MockResponse().setResponseCode(404));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        //and
        CustomAppIdentifyTrait trait = new CustomAppIdentifyTrait();
        trait.setX("valueX");
        trait.setY(234567);

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, trait);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"active\":true,\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void identifyEndpointFullOptionsTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And
        CustomAppIdentifyTrait trait = new CustomAppIdentifyTrait();
        trait.setX("valueX");
        trait.setY(234567);

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, trait, true);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"active\":true,\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8());
    }

}