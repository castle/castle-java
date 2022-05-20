package io.castle.client;

import com.google.gson.JsonParser;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.generated.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.threeten.bp.OffsetDateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class CastleFilterHttpTest extends AbstractCastleHttpLayerTest {

    public CastleFilterHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void risk() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "\"risk\": 0.65,\n" +
                "\"policy\": {\n" +
                "\"name\": \"Challenge risk >= 60\",\n" +
                "\"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "\"revision_id\": \"900b183a-9f6d-4579-8c47-9ddcccf637b4\",\n" +
                "\"action\": \"challenge\"\n" +
                "},\n" +
                "\"signals\": {\n" +
                "\"bot_behavior\": { },\n" +
                "\"proxy_ip\": { },\n" +
                "\"disposable_email\": { },\n" +
                "\"spoofed_device\": { },\n" +
                "\"multiple_accounts_per_device\": { }\n" +
                "}\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        Filter filter = new Filter()
                .type(Filter.TypeEnum.CHALLENGE)
                .status(Filter.StatusEnum.REQUESTED)
                .requestToken("4-ugt5CFooaxt5KbpISi1Kurm5KTpqawlYmFs5PXsqKootPgRB3z12OpvPOWOQ9PkztagtqicAnk9Qowu7FlU9qabyi4k2QR6KUUL5p3gr-A2w8Ju8gWe0XyRi_OkmFj2oZiU9OTPAjijjIK4sA-a7f19GC_xzhYurdkWM-ZY1jR_l4R8JloVdGTfj7IhXY6_pd5SNGThjmM2DoSjWNup74xC3v-l3lI0ZMlDZPGJAyd3jsVnd5JXc6CZlmdxSQMk8UxHPyYbk7Sn24cjMQxHPqZZVvRkypP2Z1VW82eZVLYwD5jxc48Y4vCI4C1gDJWiIVMXssRDTmrPME9aeZPSc-ZelmSpX5T3p1iU9Gb1jnYmCdp7gnJ");

        User user = new User()
                .id("12345");
        filter.user(user);

        Context context = new Context()
                .ip("211.96.77.55")
                .addHeadersItem("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15");
        filter.context(context);

        Product product = new Product()
                .id("1234");
        filter.product(product);

        AuthenticationMethod authenticationMethod = new AuthenticationMethod()
                .type(AuthenticationMethodType.SOCIAL)
                .variant("facebook");
        filter.authenticationMethod(authenticationMethod);

        filter.putPropertiesItem("property1", new HashMap<String, Object>());
        filter.putPropertiesItem("property2", new HashMap<String, Object>());

        filter.createdAt(OffsetDateTime.parse("2022-05-20T09:03:27.468+02:00"));

        FilterResponse response = sdk.onRequest(request).filter(filter);

        // Check response object
        Assert.assertEquals(response.getRisk(), 0.65, 0);
        Assert.assertEquals(response.getSignals().size(), 5);
        Assert.assertEquals(response.getPolicy().getAction(), Policy.ActionEnum.CHALLENGE);
        Assert.assertEquals(response.getPolicy().getId(), "2ee938c8-24c2-4c26-9d25-19511dd75029");
        Assert.assertEquals(response.getPolicy().getRevisionId(), "900b183a-9f6d-4579-8c47-9ddcccf637b4");
        Assert.assertEquals(response.getPolicy().getName(), "Challenge risk >= 60");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/filter"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());

        String body = recordedRequest.getBody().readUtf8();

        String expected = "{\"context\":{\"headers\":[[\"User-Agent\",\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15\"]],\"ip\":\"211.96.77.55\"},\"properties\":{\"property2\":{},\"property1\":{}},\"product\":{\"id\":\"1234\"},\"created_at\":\"2022-05-20T09:03:27.468+02:00\",\"request_token\":\"4-ugt5CFooaxt5KbpISi1Kurm5KTpqawlYmFs5PXsqKootPgRB3z12OpvPOWOQ9PkztagtqicAnk9Qowu7FlU9qabyi4k2QR6KUUL5p3gr-A2w8Ju8gWe0XyRi_OkmFj2oZiU9OTPAjijjIK4sA-a7f19GC_xzhYurdkWM-ZY1jR_l4R8JloVdGTfj7IhXY6_pd5SNGThjmM2DoSjWNup74xC3v-l3lI0ZMlDZPGJAyd3jsVnd5JXc6CZlmdxSQMk8UxHPyYbk7Sn24cjMQxHPqZZVvRkypP2Z1VW82eZVLYwD5jxc48Y4vCI4C1gDJWiIVMXssRDTmrPME9aeZPSc-ZelmSpX5T3p1iU9Gb1jnYmCdp7gnJ\",\"user\":{\"id\":\"12345\"},\"type\":\"$challenge\",\"status\":\"$requested\",\"authentication_method\":{\"type\":\"$social\",\"variant\":\"facebook\"}}";
        Assertions.assertThat(JsonParser.parseString(body)).isEqualTo(JsonParser.parseString(expected));
    }
}
