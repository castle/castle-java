package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.model.CastleContext;
import io.castle.client.internal.model.CastleHeader;
import io.castle.client.internal.model.CastleHeaders;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CastleContextBuilderTest {

    CastleContext expectedContext;
    MockHttpServletRequest request;
    CastleContextBuilder builder;
    String clientId;
    CastleConfigurationBuilder castleConfigurationBuilder;
    List<CastleHeader> listOfHeaders;
    CastleHeaders expectedHeaders;

    @Before
    public void prepare() {

        //given
        request = new MockHttpServletRequest();
        builder = new CastleContextBuilder();

        String valueContolCache = "max-age=0";
        String keyControlCache = "Cache-Control";
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";
        String ip = "8.8.8.8";
        String userAgentHeader = "User-Agent";
        String customClientIdHeader = "X-Castle-Client-Id";
        String acceptHeader = "Accept";
        String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        String AcceptLanguageHeader = "Accept-Language";
        String acceptLanguage = "en-US,en;q=0.5";
        String connectionHeader = "Connection";
        String connection = "keep-alive";
        String refererHeader = "Referer";
        String referer = "http://localhost:8080/";
        String Header = "Host";
        String host = "localhost:8080";
        String acceptEncodingHeader = "Accept-Encoding";
        String acceptEncoding = "gzip, deflate";

        clientId = "vblxmkKK-6__54sd44d";

        request.setRemoteAddr(ip);
        request.addHeader(keyControlCache, valueContolCache);
        request.addHeader(userAgentHeader, userAgent);
//        request.addHeader(customClientIdHeader, clientId);
        request.addHeader(acceptHeader, accept);
        request.addHeader(AcceptLanguageHeader, acceptLanguage);
        request.addHeader(acceptEncodingHeader, acceptEncoding);
        request.addHeader(Header, host);
        request.addHeader(refererHeader, referer);
        request.addHeader(connectionHeader, connection);

        listOfHeaders = new ArrayList<>();
        listOfHeaders.add(new CastleHeader(keyControlCache, valueContolCache));
//        listOfHeaders.add(new CastleHeader(userAgentHeader, userAgent));
//        listOfHeaders.add(new CastleHeader(customClientIdHeader, clientId));
        listOfHeaders.add(new CastleHeader(acceptHeader, accept));
        listOfHeaders.add(new CastleHeader(AcceptLanguageHeader, acceptLanguage));
        listOfHeaders.add(new CastleHeader(acceptEncodingHeader, acceptEncoding));
        listOfHeaders.add(new CastleHeader(Header, host));
        listOfHeaders.add(new CastleHeader(refererHeader, referer));
        listOfHeaders.add(new CastleHeader(connectionHeader, connection));
        expectedHeaders = new CastleHeaders();
        expectedHeaders.setHeaders(listOfHeaders);

        expectedContext = new CastleContext();

        expectedContext.setHeaders(expectedHeaders);
        expectedContext.setUserAgent(userAgent);
        expectedContext.setIp(ip);
        expectedContext.setClientId(clientId);

        castleConfigurationBuilder = CastleConfigurationBuilder.aConfigBuilder();
        castleConfigurationBuilder
                .withDefaultFailoverStrategy()
                .withTimeout(500)
                .withApiSecret("testApiSecret");
    }

    @Test
    public void buildContextFromDefaultHTTPServletRequest() {
        //given
        MockHttpServletRequest freshRequest = new MockHttpServletRequest();
        freshRequest.setRemoteAddr(null);
        CastleContext expected = new CastleContext();
        List<CastleHeader> emptyHeadersList = new ArrayList<>();
        CastleHeaders emptyHeaders = new CastleHeaders();
        emptyHeaders.setHeaders(emptyHeadersList);
        expected.setHeaders(emptyHeaders);

        //when
        CastleContext context = builder.fromHttpServletRequest(freshRequest).build();

        //then all fields in the built context should be set to null
        //(except for headers, since the MockHttpServletRequest implementation does not allow to set null instead of an empty list)
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void buildContextFromHTTPServletRequestWithCidCookie() {

        //given
        Cookie cookie = new Cookie("__cid", clientId);
        request.setCookies(cookie);

        expectedHeaders.setHeaders(listOfHeaders);
        expectedContext.setHeaders(expectedHeaders);

        //when
        CastleContext context = builder.fromHttpServletRequest(request).build();

        // then all the headers set during setUp should be passed and the cid id should be taken from the cid cookie
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(expectedContext);
    }

    @Test
    public void buildContextFromHTTPServletRequestWithoutCidCookie() {
        //given
        String alternativeClientId = "KKDjdjjeii_kkd_-mmkkd";
        expectedContext.setClientId(alternativeClientId);
        request.addHeader("X-Castle-Client-Id", alternativeClientId);


        //when
        CastleContext context = builder.fromHttpServletRequest(request).build();

        //then all the headers set during setUp should be passed and the cid id should be taken from the custom Castle-Id header
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(expectedContext);
    }

    @Test
    public void buildContextWithEmptyConfiguration() {

        //given
        CastleConfiguration configuration = castleConfigurationBuilder
                .withBlackListHeaders(new LinkedList<String>())
                .withWhiteListHeaders(new LinkedList<String>())
                .build();

        Cookie cookie = new Cookie("__cid", clientId);
        request.setCookies(cookie);

        List<CastleHeader> emptyHeadersList = new ArrayList<>();
        CastleHeaders emptyHeaders = new CastleHeaders();
        emptyHeaders.setHeaders(emptyHeadersList);
        expectedContext.setHeaders(emptyHeaders);

        //when
        CastleContext context = builder.fromHttpServletRequest(request)
                .withConfiguration(configuration)
                .build();

        //then all headers should be left blank???
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(expectedContext);
    }

    @Test
    public void buildContextWithDefaultWhitelistHeaders() {

        //given
        CastleConfiguration configuration = castleConfigurationBuilder
                .withBlackListHeaders(new LinkedList<String>())
                .withDefaultWhitelist()
                .build();

        Cookie cookie = new Cookie("__cid", clientId);
        Cookie session = new Cookie("SESSIONID", "node0u06kx7plbtd11m246ilq8hfuh0");
        request.setCookies(session, cookie);

        CastleHeader cookiesHeader = new CastleHeader("Cookie", "JSESSIONID node0u06kx7plbtd11m246ilq8hfuh0.node0; __cid vblxmkKK-6__54sd44d");
        listOfHeaders.add(cookiesHeader);

        expectedHeaders.setHeaders(listOfHeaders);
        expectedContext.setHeaders(expectedHeaders);

        //when
        CastleContext context = builder.fromHttpServletRequest(request)
                .withConfiguration(configuration)
                .build();

        //then ????
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(expectedContext);

    }
}
