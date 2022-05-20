package io.castle.client;

import com.google.gson.JsonParser;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleResponse;
import io.castle.client.model.generated.*;
import io.castle.client.utils.DeviceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.threeten.bp.OffsetDateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;

public class CastleLogHttpTest extends AbstractCastleHttpLayerTest {

    public CastleLogHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void log() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(204);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        Log log = new Log()
                .type(Log.TypeEnum.TRANSACTION)
                .status(Log.StatusEnum.ATTEMPTED);

        User user = new User()
                .id("12345");
        log.user(user);

        Context context = new Context()
                .ip("211.96.77.55")
                .addHeadersItem("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15");
        log.context(context);

        Product product = new Product()
                .id("1234");
        log.product(product);

        log.putPropertiesItem("property1", new HashMap<String, Object>());
        log.putPropertiesItem("property2", new HashMap<String, Object>());

        log.createdAt(OffsetDateTime.parse("2022-05-20T09:03:27.468+02:00"));

        Transaction transaction = new Transaction()
                .type(Transaction.TypeEnum.PURCHASE)
                .id("900b183a-9f6d-4579-8c47-9ddcccf637b4")
                .amount(new Amount()
                        .type(Amount.TypeEnum.FIAT)
                        .currency("USD"))
                .paymentMethod(new PaymentMethod()
                        .type(PaymentMethod.TypeEnum.ABA)
                        .fingerprint("string")
                        .holderName("string")
                        .bankName("string")
                        .countryCode("str")
                        .billingAddress(new Address()
                                .line1("60 Rausch Street")
                                .line2("string")
                                .city("San Francisco")
                                .countryCode("US")
                                .regionCode("CA")
                                .postalCode("94103"))
                        .card(new PaymentMethodCard()
                                .bin("string")
                                .last4("string")
                                .expMonth(1)
                                .expYear(2000)
                                .network(PaymentMethodCard.NetworkEnum.AMEX)
                                .funding(PaymentMethodCard.FundingEnum.CREDIT)));
        log.transaction(transaction);

        CastleResponse response = sdk.onRequest(request).log(log);

        Assert.assertTrue(response.isSuccessful());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/log"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());

        String body = recordedRequest.getBody().readUtf8();

        String expected = "{\"context\":{\"headers\":[[\"User-Agent\",\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15\"]],\"ip\":\"211.96.77.55\"},\"properties\":{\"property2\":{},\"property1\":{}},\"product\":{\"id\":\"1234\"},\"created_at\":\"2022-05-20T09:03:27.468+02:00\",\"user\":{\"id\":\"12345\"},\"type\":\"$transaction\",\"status\":\"$attempted\",\"transaction\":{\"type\":\"$purchase\",\"id\":\"900b183a-9f6d-4579-8c47-9ddcccf637b4\",\"amount\":{\"type\":\"$fiat\",\"currency\":\"USD\"},\"payment_method\":{\"type\":\"$aba\",\"fingerprint\":\"string\",\"holder_name\":\"string\",\"bank_name\":\"string\",\"country_code\":\"str\",\"billing_address\":{\"line1\":\"60 Rausch Street\",\"line2\":\"string\",\"city\":\"San Francisco\",\"country_code\":\"US\",\"region_code\":\"CA\",\"postal_code\":\"94103\"},\"card\":{\"bin\":\"string\",\"last4\":\"string\",\"exp_month\":1,\"exp_year\":2000,\"network\":\"$amex\",\"funding\":\"$credit\"}}}}";
        Assertions.assertThat(JsonParser.parseString(body)).isEqualTo(JsonParser.parseString(expected));
    }
}
