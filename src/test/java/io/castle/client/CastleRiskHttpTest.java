package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleResponse;
import io.castle.client.model.generated.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class CastleRiskHttpTest extends AbstractCastleHttpLayerTest {

    public CastleRiskHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void risk() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"risk\": 0.65,\n" +
                "  \"policy\": {\n" +
                "    \"name\": \"Challenge risk >= 60\",\n" +
                "    \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "    \"revision_id\": \"900b183a-9f6d-4579-8c47-9ddcccf637b4\",\n" +
                "    \"action\": \"challenge\"\n" +
                "  },\n" +
                "  \"signals\": {\n" +
                "    \"bot_behavior\": {},\n" +
                "    \"proxy_ip\": {},\n" +
                "    \"disposable_email\": {},\n" +
                "    \"spoofed_device\": {},\n" +
                "    \"multiple_accounts_per_device\": {}\n" +
                "  },\n" +
                "  \"device\": {\n" +
                "    \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjEzc2x6RzNHQ0RzeFJCejdJWF9SUDJkV1Y0RFgiLCJxdWFsaWZpZXIiOiJBUUlEQ2pFME5EZzFPREF3T1RZIiwiYW5vbnltb3VzIjpmYWxzZSwidmVyc2lvbiI6MC4zfQ.y3vOt-W1IpOi7Oyn1jll1uDw1YL-JPZtNMTU-PyaYhQ\"\n" +
                "  }\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        Risk risk = new Risk();
        risk.type(Risk.TypeEnum.CHALLENGE);
        risk.status(Risk.StatusEnum.REQUESTED);
        risk.requestToken("test_lZWva9rsNe3u0_EIc6R8V3t5beV38piPAQbhgREGygYCAo2FRSv1tAQ4-cb6ArKHOWK_zG18hO1uZ8K0LDbNqU9njuhscoLyaj3NyGxyiO0iS4ziIkm-oVom3LEsN9i6InSbuzo-w7ErJqrkYW2CrjA23LEyN92wIkCE82dggvktPtWvMmrl42Bj2uM7Zdn2AQGXC6qGTIECRlwaAgZcgcAGeX4");

        User user = new User();
        user.id("12345");
        risk.user(user);

        Context context = new Context();
        context.ip("211.96.77.55");
        context.addHeadersItem("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15");
        risk.context(context);

        RiskResponse response = sdk.onRequest(request).risk(risk);

        // Check response object
        Assert.assertEquals(response.getRisk(), 0.65, 0);
        Assert.assertEquals(response.getSignals().size(), 5);
        Assert.assertEquals(response.getDevice().getToken(), "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjEzc2x6RzNHQ0RzeFJCejdJWF9SUDJkV1Y0RFgiLCJxdWFsaWZpZXIiOiJBUUlEQ2pFME5EZzFPREF3T1RZIiwiYW5vbnltb3VzIjpmYWxzZSwidmVyc2lvbiI6MC4zfQ.y3vOt-W1IpOi7Oyn1jll1uDw1YL-JPZtNMTU-PyaYhQ");
        Assert.assertEquals(response.getPolicy().getAction(), Policy.ActionEnum.CHALLENGE);
        Assert.assertEquals(response.getPolicy().getId(), "2ee938c8-24c2-4c26-9d25-19511dd75029");
        Assert.assertEquals(response.getPolicy().getRevisionId(), "900b183a-9f6d-4579-8c47-9ddcccf637b4");
        Assert.assertEquals(response.getPolicy().getName(), "Challenge risk >= 60");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/risk"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }
}
