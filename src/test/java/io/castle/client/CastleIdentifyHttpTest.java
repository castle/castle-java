package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class CastleIdentifyHttpTest extends AbstractCastleHttpLayerTest {

    public CastleIdentifyHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void identifyEndpointTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // when and an identify request is made
        sdk.onRequest(request).identify(id);

        // then the json match specification
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void identifyEndpointWithTraitObjectTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and
        CustomAppTraits trait = new CustomAppTraits();
        trait.setX("valueX");
        trait.setY(234567);

        // when an identify request is made with a trait object
        sdk.onRequest(request).identify(id, trait);

        // then the json match specification
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
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
        CustomAppTraits trait = new CustomAppTraits();
        trait.setX("valueX");
        trait.setY(234567);

        // when an identify request is made with a trait object and active option
        sdk.onRequest(request).identify(id, trait, false);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":false,\"ip\":\"127.0.0.1\",\"headers\":{\"REMOTE_ADDR\":\"127.0.0.1\"},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void identifyEndpointFailuresAreIgnoredTest() throws InterruptedException {
        // given backend response is not 200
        server.enqueue(new MockResponse().setResponseCode(404));
        String id = "12345";

        // and a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        //and
        CustomAppTraits trait = new CustomAppTraits();
        trait.setX("valueX");
        trait.setY(234567);

        // when an identify request is made with a trait object
        sdk.onRequest(request).identify(id, trait);

        // then no exceptions are throw
    }

}