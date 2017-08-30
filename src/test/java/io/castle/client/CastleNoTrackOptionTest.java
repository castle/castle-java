package io.castle.client;

import io.castle.client.api.CastleApi;
import okhttp3.mockwebserver.MockResponse;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class CastleNoTrackOptionTest extends AbstractCastleHttpLayerTest {

    @Test
    public void noTrackOptionDisableRequests() throws InterruptedException {

        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And response from authenticate is prepared
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(true);
        //When all API call are executed
        castleApi.track("testEvent");
        castleApi.identify("userId", true);
        castleApi.authenticate("testEvent", "userId");

        //Then only the authenticate call is executed
        server.takeRequest();
        Assertions.assertThat(server.getRequestCount()).isEqualTo(1);
    }

    @Test
    public void trackOptionAllowRequests() throws InterruptedException {

        //Given a mock HTTP request is provided
        HttpServletRequest request = new MockHttpServletRequest();
        //And responses are setup on mock server
        server.enqueue(new MockResponse().setResponseCode(200));
        server.enqueue(new MockResponse().setResponseCode(200));
        server.enqueue(new MockResponse().setBody(
                "{\n" +
                        "  \"action\": \"deny\",\n" +
                        "  \"user_id\": \"12345\"\n" +
                        "}"));
        //And a CastleApi is created with doNotTrack option
        CastleApi castleApi = sdk.onRequest(request).doNotTrack(false);
        //When all API call are executed
        castleApi.track("testEvent");
        server.takeRequest();
        castleApi.identify("userId", true);
        server.takeRequest();
        castleApi.authenticate("testEvent", "userId");
        server.takeRequest();

        //Then all the calls are executed
        Assertions.assertThat(server.getRequestCount()).isEqualTo(3);
    }
}
