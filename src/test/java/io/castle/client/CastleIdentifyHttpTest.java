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

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, true);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
                recordedRequest.getBody().readUtf8());
    }

    @Test
    public void identifyEndpointFailuresAreIgnoredTest() throws InterruptedException {
        //given
        server.enqueue(new MockResponse().setResponseCode(404));
        String id = "12345";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, true);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}}",
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
        CustomAppProperties properties = new CustomAppProperties();
        properties.setA("valueA");
        properties.setB(123456);
        CustomAppIdentifyTrait trait = new CustomAppIdentifyTrait();
        trait.setX("valueX");
        trait.setY(234567);

        // and an authenticate request is made
        sdk.onRequest(request).identify(id, true, trait, properties);

        // then the dumb backend should return the default Authenticate.Action in the configuration
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}},\"traits\":{\"x\":\"valueX\",\"y\":234567},\"properties\":{\"a\":\"valueA\",\"b\":123456}}",
                recordedRequest.getBody().readUtf8());
    }


    private static class CustomAppProperties {
        private String a;
        private int b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    private static class CustomAppIdentifyTrait {
        private String x;
        private int y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}