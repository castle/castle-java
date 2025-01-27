package io.castle.client;

import com.google.gson.JsonParser;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.generated.*;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.threeten.bp.OffsetDateTime;

import java.util.HashMap;
import java.util.List;

public class CastleFilterHttpTest extends AbstractCastleHttpLayerTest {

    public CastleFilterHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test public void filter() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"risk\": 0.65,\n" +
                "  \"scores\": {\n" +
                "    \"account_abuse\": {\n" +
                "      \"score\": 0.65\n" +
                "    },\n" +
                "    \"account_takeover\": {\n" +
                "      \"score\": 0.77\n" +
                "    },\n" +
                "    \"bot\": {\n" +
                "      \"score\": 0.34\n" +
                "    }\n" +
                "  },\n" +
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
                "  \"metrics\": {\n" +
                "    \"1\": {},\n" +
                "    \"2\": {},\n" +
                "    \"3\": {},\n" +
                "    \"4\": {},\n" +
                "    \"5\": {}\n" +
                "  },\n" +
                "  \"device\": {\n" +
                "    \"user_agent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36\",\n" +
                "    \"fingerprint\": \"ohvjn8adSnetYTzZ8B7bOP\",\n" +
                "    \"hardware\": {\n" +
                "      \"type\": \"computer\",\n" +
                "      \"name\": null,\n" +
                "      \"brand\": null,\n" +
                "      \"display\": {\n" +
                "        \"width\": 1512,\n" +
                "        \"height\": 982\n" +
                "      },\n" +
                "      \"model\": {\n" +
                "        \"name\": null,\n" +
                "        \"code\": null\n" +
                "      }\n" +
                "    },\n" +
                "    \"os\": {\n" +
                "      \"name\": \"Windows 10\",\n" +
                "      \"version\": {\n" +
                "        \"major\": \"10\",\n" +
                "        \"full\": null\n" +
                "      }\n" +
                "    },\n" +
                "    \"software\": {\n" +
                "      \"type\": \"browser\",\n" +
                "      \"name\": \"Chrome\",\n" +
                "      \"languages\": [\n" +
                "        \"en-us\",\n" +
                "        \"en\"\n" +
                "      ],\n" +
                "      \"version\": {\n" +
                "        \"major\": \"91\",\n" +
                "        \"full\": \"91.0.4472\"\n" +
                "      },\n" +
                "      \"fingerprint\": \"vOch_0a_fpkl1Tf-pVPuDA\"\n" +
                "    },\n" +
                "    \"timezone\": {\n" +
                "      \"offset\": -300,\n" +
                "      \"name\": \"America/New_York\"\n" +
                "    },\n" +
                "    \"screen\": {\n" +
                "      \"screen\": 2,\n" +
                "      \"orientation\": \"landscape\"\n" +
                "    },\n" +
                "    \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjEzc2x6RzNHQ0RzeFJCejdJWF9SUDJkV1Y0RFgiLCJxdWFsaWZpZXIiOiJBUUlEQ2pFME5EZzFPREF3T1RZIiwiYW5vbnltb3VzIjpmYWxzZSwidmVyc2lvbiI6MC4zfQ.y3vOt-W1IpOi7Oyn1jll1uDw1YL-JPZtNMTU-PyaYhQ\"\n" +
                "  },\n" +
                "  \"id\": \"ASZoelALT5-PaVw2pAVMXg\",\n" +
                "  \"name\": \"Login Succeeded\",\n" +
                "  \"type\": \"$challenge\",\n" +
                "  \"status\": \"$succeeded\",\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"authenticated\": true,\n" +
                "  \"authentication_method\": {\n" +
                "    \"type\": \"$social\",\n" +
                "    \"variant\": \"facebook\"\n" +
                "  },\n" +
                "  \"email\": {\n" +
                "    \"normalized\": \"user.email@example.com\",\n" +
                "    \"domain\": \"gmail.com\",\n" +
                "    \"disposable\": false,\n" +
                "    \"unreachable\": false,\n" +
                "    \"domain_details\": {\n" +
                "      \"created_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "      \"updated_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "      \"expires_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "      \"registrar\": \"string\",\n" +
                "      \"nameservers\": [\n" +
                "        \"ns1.hosting.com\",\n" +
                "        \"ns2.hosting.com\",\n" +
                "        \"ns3.hosting.com\"\n" +
                "      ],\n" +
                "      \"spf_record\": {\n" +
                "        \"exists\": false\n" +
                "      },\n" +
                "      \"dmarc_record\": {\n" +
                "        \"exists\": false\n" +
                "      },\n" +
                "      \"mx_records\": {\n" +
                "        \"null_mx\": false\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"endpoint\": \"/v1/risk\",\n" +
                "  \"ip\": {\n" +
                "    \"asn\": 14618,\n" +
                "    \"location\": {\n" +
                "      \"city\": \"Ashburn\",\n" +
                "      \"continent_code\": \"NA\",\n" +
                "      \"country_code\": \"US\",\n" +
                "      \"postal_code\": \"20149\",\n" +
                "      \"region_code\": \"VA\",\n" +
                "      \"latitude\": 52.3583,\n" +
                "      \"longitude\": 4.8488\n" +
                "    },\n" +
                "    \"address\": \"34.200.81.5\",\n" +
                "    \"isp\": {\n" +
                "      \"name\": \"verizon fios\",\n" +
                "      \"organization\": \"verizon fios\"\n" +
                "    },\n" +
                "    \"type\": \"ipv4\",\n" +
                "    \"privacy\": {\n" +
                "      \"anonymous\": false,\n" +
                "      \"datacenter\": false,\n" +
                "      \"proxy\": false,\n" +
                "      \"tor\": false\n" +
                "    }\n" +
                "  },\n" +
                "  \"params\": {\n" +
                "    \"email\": \"Rhea.Franecki@example.org\",\n" +
                "    \"phone\": \"+16175551212\",\n" +
                "    \"username\": \"superhero123\"\n" +
                "  },\n" +
                "  \"product\": {\n" +
                "    \"id\": \"string\"\n" +
                "  },\n" +
                "  \"sdks\": {\n" +
                "    \"client\": {\n" +
                "      \"name\": \"castle-web\",\n" +
                "      \"version\": \"2.0.0\"\n" +
                "    },\n" +
                "    \"server\": {\n" +
                "      \"name\": \"castle-postman\",\n" +
                "      \"version\": \"0.1.0\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"id\": \"string\",\n" +
                "    \"created_at\": \"2019-08-24T14:15:22Z\"\n" +
                "  },\n" +
                "  \"user\": {\n" +
                "    \"id\": \"string\",\n" +
                "    \"email\": \"пошта@укр.нет\",\n" +
                "    \"phone\": \"string\",\n" +
                "    \"registered_at\": \"2019-08-24T14:15:22Z\",\n" +
                "    \"name\": \"string\",\n" +
                "    \"traits\": {},\n" +
                "    \"address\": {\n" +
                "      \"line1\": \"60 Rausch Street\",\n" +
                "      \"line2\": \"string\",\n" +
                "      \"city\": \"San Francisco\",\n" +
                "      \"country_code\": \"US\",\n" +
                "      \"region_code\": \"CA\",\n" +
                "      \"postal_code\": \"94103\",\n" +
                "      \"fingerprint\": \"8a33j2lir9\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"behavior\": {\n" +
                "    \"fingerprint\": \"vOch_0a_fpkl1Tf-pVPuDA\"\n" +
                "  },\n" +
                "  \"properties\": {}\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        Filter filter = createFilter();

        FilterAndRiskResponse response = sdk.onRequest(request).filter(filter);

        // Check response object
        Assert.assertNotNull(response.getRisk());
        Assert.assertEquals(0.65, response.getRisk(), 0);
        Assert.assertEquals(5, response.getSignals().size());
        Assert.assertEquals(Policy.ActionEnum.CHALLENGE, response.getPolicy().getAction());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getPolicy().getId());
        Assert.assertEquals("900b183a-9f6d-4579-8c47-9ddcccf637b4", response.getPolicy().getRevisionId());
        Assert.assertEquals("Challenge risk >= 60", response.getPolicy().getName());
        Assert.assertNotNull(response.getScores());
        Assert.assertEquals(0.65, response.getScores().getAccountAbuse().getScore(), 0.0);
        Assert.assertEquals(0.77, response.getScores().getAccountTakeover().getScore(), 0.0);
        Assert.assertEquals(0.34, response.getScores().getBot().getScore(), 0.0);
        Assert.assertEquals(5, response.getMetrics().size());
        Assert.assertEquals("ohvjn8adSnetYTzZ8B7bOP", response.getDevice().getFingerprint());
        //Assert.assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjEzc2x6RzNHQ0RzeFJCejdJWF9SUDJkV1Y0RFgiLCJxdWFsaWZpZXIiOiJBUUlEQ2pFME5EZzFPREF3T1RZIiwiYW5vbnltb3VzIjpmYWxzZSwidmVyc2lvbiI6MC4zfQ.y3vOt-W1IpOi7Oyn1jll1uDw1YL-JPZtNMTU-PyaYhQ", response.getDevice().getHardware().getToken());
        Assert.assertEquals("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36", response.getDevice().getUserAgent());
        Assert.assertEquals("Windows 10", response.getDevice().getOs().getName());
        Assert.assertEquals("10", response.getDevice().getOs().getVersion().getMajor());
        Assert.assertEquals("Chrome", response.getDevice().getSoftware().getName());
        Assert.assertEquals("browser", response.getDevice().getSoftware().getType());
        Assert.assertEquals("en-us", response.getDevice().getSoftware().getLanguages().get(0));
        Assert.assertEquals("en", response.getDevice().getSoftware().getLanguages().get(1));
        Assert.assertEquals("91.0.4472", response.getDevice().getSoftware().getVersion().getFull());
        Assert.assertEquals("91", response.getDevice().getSoftware().getVersion().getMajor());
        Assert.assertEquals(2, response.getDevice().getScreen().getScreen());
        Assert.assertEquals("landscape", response.getDevice().getScreen().getOrientation());
        Assert.assertEquals("America/New_York", response.getDevice().getTimezone().getName());
        Assert.assertEquals(-300, response.getDevice().getTimezone().getOffset());
        Assert.assertEquals("ASZoelALT5-PaVw2pAVMXg", response.getId());
        Assert.assertEquals("Login Succeeded", response.getName());
        Assert.assertEquals("$challenge", response.getType());
        Assert.assertEquals("$succeeded", response.getStatus());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());
        Assert.assertTrue(response.isAuthenticated());
        Assert.assertEquals(AuthenticationMethodType.SOCIAL, response.getAuthenticationMethod().getType());
        Assert.assertEquals("facebook", response.getAuthenticationMethod().getVariant());
        Assert.assertEquals("user.email@example.com", response.getEmail().getNormalized());
        Assert.assertEquals("gmail.com", response.getEmail().getDomain());
        Assert.assertFalse(response.getEmail().isDisposable());
        Assert.assertFalse(response.getEmail().isUnreachable());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getEmail().getDomainDetails().getCreatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getEmail().getDomainDetails().getUpdatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getEmail().getDomainDetails().getExpiresAt());
        Assert.assertEquals("string", response.getEmail().getDomainDetails().getRegistrar());
        Assert.assertEquals(3, response.getEmail().getDomainDetails().getNameservers().size());
        Assert.assertEquals("ns1.hosting.com", response.getEmail().getDomainDetails().getNameservers().get(0));
        Assert.assertEquals("ns2.hosting.com", response.getEmail().getDomainDetails().getNameservers().get(1));
        Assert.assertEquals("ns3.hosting.com", response.getEmail().getDomainDetails().getNameservers().get(2));
        Assert.assertFalse(response.getEmail().getDomainDetails().getSpfRecord().isExists());
        Assert.assertFalse(response.getEmail().getDomainDetails().getDmarcRecord().isExists());
        Assert.assertFalse(response.getEmail().getDomainDetails().getMxRecords().isNullMx());
        Assert.assertEquals("/v1/risk", response.getEndpoint());
        Assert.assertEquals(14618, response.getIp().getAsn());
        Assert.assertEquals("Ashburn", response.getIp().getLocation().getCity());
        Assert.assertEquals("NA", response.getIp().getLocation().getContinentCode());
        Assert.assertEquals("US", response.getIp().getLocation().getCountryCode());
        Assert.assertEquals("20149", response.getIp().getLocation().getPostalCode());
        Assert.assertEquals("VA", response.getIp().getLocation().getRegionCode());
        Assert.assertEquals(52.3583, response.getIp().getLocation().getLatitude(), 0.0);
        Assert.assertEquals(4.8488, response.getIp().getLocation().getLongitude(), 0.0);
        Assert.assertEquals("34.200.81.5", response.getIp().getAddress());
        Assert.assertEquals("verizon fios", response.getIp().getIsp().getName());
        Assert.assertEquals("verizon fios", response.getIp().getIsp().getOrganization());
        Assert.assertEquals("ipv4", response.getIp().getType());
        Assert.assertFalse(response.getIp().getPrivacy().isAnonymous());
        Assert.assertFalse(response.getIp().getPrivacy().isDatacenter());
        Assert.assertFalse(response.getIp().getPrivacy().isProxy());
        Assert.assertFalse(response.getIp().getPrivacy().isTor());
        Assert.assertEquals("string", response.getProduct().getId());
        Assert.assertEquals("Rhea.Franecki@example.org", response.getParams().getEmail());
        Assert.assertEquals("+16175551212", response.getParams().getPhone());
        Assert.assertEquals("superhero123", response.getParams().getUsername());
        Assert.assertEquals("castle-web", response.getSdks().getClient().getName());
        Assert.assertEquals("2.0.0", response.getSdks().getClient().getVersion());
        Assert.assertEquals("castle-postman", response.getSdks().getServer().getName());
        Assert.assertEquals("0.1.0", response.getSdks().getServer().getVersion());
        Assert.assertEquals("string", response.getUser().getId());
        Assert.assertEquals("пошта@укр.нет", response.getUser().getEmail());
        Assert.assertEquals("string", response.getUser().getPhone());
        Assert.assertEquals(OffsetDateTime.parse("2019-08-24T14:15:22Z"), response.getUser().getRegisteredAt());
        Assert.assertEquals("string", response.getUser().getName());
        Assert.assertEquals(0, response.getUser().getTraits().size());
        Assert.assertEquals("60 Rausch Street", response.getUser().getAddress().getLine1());
        Assert.assertEquals("string", response.getUser().getAddress().getLine2());
        Assert.assertEquals("San Francisco", response.getUser().getAddress().getCity());
        Assert.assertEquals("US", response.getUser().getAddress().getCountryCode());
        Assert.assertEquals("CA", response.getUser().getAddress().getRegionCode());
        Assert.assertEquals("94103", response.getUser().getAddress().getPostalCode());
        Assert.assertEquals("8a33j2lir9", response.getUser().getAddress().getFingerprint());
        Assert.assertEquals("vOch_0a_fpkl1Tf-pVPuDA", response.getBehavior().getFingerprint());
        Assert.assertEquals(0, response.getProperties().size());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/filter"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    private Filter createFilter() {
        Filter filter = new Filter()
                .type(Filter.TypeEnum.LOGIN)
                .status(Filter.StatusEnum.ATTEMPTED)
                .requestToken("test_lZWva9rsNe3u0_EIc6R8V3t5beV38piPAQbhgREGygYCAo2FRSv1tAQ4-cb6ArKHOWK_zG18hO1uZ8K0LDbNqU9njuhscoLyaj3NyGxyiO0iS4ziIkm-oVom3LEsN9i6InSbuzo-w7ErJqrkYW2CrjA23LEyN92wIkCE82dggvktPtWvMmrl42Bj2uM7Zdn2AQGXC6qGTIECRlwaAgZcgcAGeX4");

        User user = new User()
                .email("Rhea.Franecki@example.org")
                .phone("+16175551212");
        filter.user(user);

        FilterRequestParams filterRequestParams = new FilterRequestParams()
                .email("Rhea.Franecki@example.org")
                .phone("+16175551212")
                .username("superhero123");
        filter.setParams(filterRequestParams);

        Context context = new Context()
                .ip("211.96.77.55")
                .addHeadersItem("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15")
                .addHeadersItem("Accept-Encoding", "gzip, deflate, br")
                .addHeadersItem("Accept-Language", "en-us")
                .addHeadersItem("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeadersItem("Connection", "close")
                .addHeadersItem("Host", "castle.io");
        filter.context(context);

        Product product = new Product()
                .id("string");
        filter.product(product);

        Session session = new Session()
                .id("string")
                .createdAt(OffsetDateTime.parse("2019-08-24T14:15:22Z"));
        filter.session(session);

        AuthenticationMethod authenticationMethod = new AuthenticationMethod()
                .type(AuthenticationMethodType.SOCIAL)
                .variant("facebook");
        filter.authenticationMethod(authenticationMethod);

        filter.putPropertiesItem("property1", new HashMap<String, Object>());
        filter.putPropertiesItem("property2", new HashMap<String, Object>());

        filter.createdAt(OffsetDateTime.parse("2019-08-24T14:15:22Z"));

        filter.matchingUserId("123");
        filter.skipRequestTokenValidation(false);
        filter.skipContextValidation(false);
        filter.expand(List.of("all"));

        return filter;
    }

    @Test
    public void compareFilterJson() {
        Filter filter = createFilter();

        // Convert filter object to JSON
        CastleGsonModel gson = new CastleGsonModel();
        String filterJson = gson.getGson().toJson(filter);

        // Provided JSON
        String providedJson = "{\n" +
                "  \"context\": {\n" +
                "    \"headers\": [\n" +
                "      [\n" +
                "        \"User-Agent\",\n" +
                "        \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15\"\n" +
                "      ],\n" +
                "      [\n" +
                "        \"Accept-Encoding\",\n" +
                "        \"gzip, deflate, br\"\n" +
                "      ],\n" +
                "      [\n" +
                "        \"Accept-Language\",\n" +
                "        \"en-us\"\n" +
                "      ],\n" +
                "      [\n" +
                "        \"Accept\",\n" +
                "        \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\"\n" +
                "      ],\n" +
                "      [\n" +
                "        \"Connection\",\n" +
                "        \"close\"\n" +
                "      ],\n" +
                "      [\n" +
                "        \"Host\",\n" +
                "        \"castle.io\"\n" +
                "      ]\n" +
                "    ],\n" +
                "    \"ip\": \"211.96.77.55\"\n" +
                "  },\n" +
                "  \"properties\": {\n" +
                "    \"property1\": {},\n" +
                "    \"property2\": {}\n" +
                "  },\n" +
                "  \"product\": {\n" +
                "    \"id\": \"string\"\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"id\": \"string\",\n" +
                "    \"created_at\": \"2019-08-24T14:15:22Z\"\n" +
                "  },\n" +
                "  \"created_at\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"request_token\": \"test_lZWva9rsNe3u0_EIc6R8V3t5beV38piPAQbhgREGygYCAo2FRSv1tAQ4-cb6ArKHOWK_zG18hO1uZ8K0LDbNqU9njuhscoLyaj3NyGxyiO0iS4ziIkm-oVom3LEsN9i6InSbuzo-w7ErJqrkYW2CrjA23LEyN92wIkCE82dggvktPtWvMmrl42Bj2uM7Zdn2AQGXC6qGTIECRlwaAgZcgcAGeX4\",\n" +
                "  \"user\": {\n" +
                "    \"email\": \"Rhea.Franecki@example.org\",\n" +
                "    \"phone\": \"+16175551212\"\n" +
                "  },\n" +
                "  \"params\": {\n" +
                "    \"email\": \"Rhea.Franecki@example.org\",\n" +
                "    \"phone\": \"+16175551212\",\n" +
                "    \"username\": \"superhero123\"\n" +
                "  },\n" +
                "  \"matching_user_id\": \"123\",\n" +
                "  \"skip_request_token_validation\": false,\n" +
                "  \"skip_context_validation\": false,\n" +
                "  \"expand\": [\n" +
                "    \"all\"\n" +
                "  ],\n" +
                "  \"type\": \"$login\",\n" +
                "  \"status\": \"$attempted\",\n" +
                "  \"authentication_method\": {\n" +
                "    \"type\": \"$social\",\n" +
                "    \"variant\": \"facebook\"\n" +
                "  }\n" +
                "}";

        // Compare the JSON strings
        Assert.assertEquals(JsonParser.parseString(providedJson), JsonParser.parseString(filterJson));
    }
}
