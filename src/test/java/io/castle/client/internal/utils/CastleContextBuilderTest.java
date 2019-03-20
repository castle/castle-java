package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.*;
import io.castle.client.utils.SDKVersion;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


public class CastleContextBuilderTest {

    private final CastleGsonModel model = new CastleGsonModel();

    @Test
    public void buildContextWithDefaultSetup() throws CastleSdkConfigurationException {

        //Given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();
        CastleContext standardContext = getStandardContext();

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest)
                .device(getStandardDevice())
                .build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void blockHeadersOnBlackList() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withBlackListHeaders("Cookie", acceptLanguageHeader)
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model)
                .device(getStandardDevice());
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And a expected castle context without the accept-language header
        CastleContext standardContext = getStandardContext();
        List<CastleHeader> listOfHeaders = new ArrayList<>();
        for (CastleHeader header : standardContext.getHeaders().getHeaders()) {
            if (!header.getKey().equals("Cookie") && !header.getKey().equals(acceptLanguageHeader)) {
                listOfHeaders.add(header);
            } else {
                listOfHeaders.add(new CastleHeader(header.getKey(), "true"));
            }
        }
        standardContext.getHeaders().setHeaders(listOfHeaders);

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest).build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void blackListIsMoreRelevantThatWhitelist() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withBlackListHeaders(connectionHeader)
                .withWhiteListHeaders(connectionHeader)
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And a expected castle context
        CastleContext standardContext = getStandardScrubbedContext();
        List<CastleHeader> listOfHeaders = new ArrayList<>();
        for (CastleHeader header : standardContext.getHeaders().getHeaders()) {
            if (!header.getKey().equals(connectionHeader)) {
                listOfHeaders.add(header);
            } else {
                listOfHeaders.add(new CastleHeader(header.getKey(), "true"));
            }
        }
        standardContext.getHeaders().setHeaders(listOfHeaders);

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest)
                .device(getStandardDevice())
                .build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void whiteListPassHeadersToTheContext() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withDefaultBlacklist()
                .withWhiteListHeaders(connectionHeader)
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model)
                .device(getStandardDevice());
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And a expected castle context with a single whitelisted header
        CastleContext standardContext = getStandardScrubbedContext();
        for (CastleHeader header : standardContext.getHeaders().getHeaders()) {
            if (header.getKey().equals(connectionHeader)) {
                header.setValue(connection);
            }
        }

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest).build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void clientIdIsInFirstPlaceTakenFromHeader() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model);

        //And a http request with __cid cookie
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        Cookie cookie = new Cookie("__cid", "valueFromCookie");
        Cookie someOtherCookie = new Cookie("Some", "cookie");
        standardRequest.setCookies(cookie, someOtherCookie);
        //And a custom castle header
        standardRequest.addHeader(customClientIdHeader, "valueFromHeaders");

        //And a expected context value with matching clientId
        CastleContext standardContext = getStandardContextWithClientId("valueFromHeaders");

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest)
                .device(getStandardDevice())
                .build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void clientIdUseFailoverValueFromHeaders() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleContextBuilder builder = new CastleContextBuilder(configuration, model);

        //And a http request without __cid cookie
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        //And a custom castle header
        standardRequest.addHeader(customClientIdHeader, "valueFromHeaders");

        //And a expected context value with matching clientId
        CastleContext standardContext = getStandardContextWithClientId("valueFromHeaders");

        //When
        CastleContext context = builder.fromHttpServletRequest(standardRequest)
                .device(getStandardDevice())
                .build();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void builderMethods() throws CastleSdkConfigurationException {
        // Given
        CastleConfiguration configuration = CastleConfigurationBuilder
            .defaultConfigBuilder()
            .withApiSecret("abcd")
            .build();

        // When
        CastleContext context = new CastleContextBuilder(configuration, model)
            .userAgent(userAgent)
            .headers(getStandardCastleHeaders())
            .ip(ip)
            .device(getStandardDevice())
            .build();
        // And
        CastleContext standardContext = getStandardContext();

        //Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void toAndFromJson() throws CastleSdkConfigurationException {
        // Given
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        CastleContext standardContext = getStandardContext();
        CastleConfiguration configuration = CastleConfigurationBuilder
            .defaultConfigBuilder()
            .withApiSecret("abcd")
            .build();

        // When
        String json = new CastleContextBuilder(configuration, model)
            .fromHttpServletRequest(standardRequest)
            .device(getStandardDevice())
            .toJson();

        CastleContext context = new CastleContextBuilder(configuration, model)
            .fromJson(json)
            .build();

        // Then
        Assertions.assertThat(context).isEqualToComparingFieldByFieldRecursively(standardContext);
    }

    @Test
    public void withManualHeaders() throws CastleSdkConfigurationException, JSONException {
        // Given
        CastleConfiguration configuration = CastleConfigurationBuilder
            .defaultConfigBuilder()
            .withApiSecret("abcd")
            .build();
        String contextJson = new CastleContextBuilder(configuration, model)
            .headers(CastleHeaders.builder()
                .add("User-Agent", "ua")
                .build()
            )
            .device(getStandardDevice())
            .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"active\":true,\"device\":{\"id\":\"d_id\",\"manufacturer\":\"d_manufacturer\",\"model\":\"d_model\",\"name\":\"d_name\",\"type\":\"d_type\"},\"headers\":{\"User-Agent\":\"ua\"}," + SDKVersion.getLibraryString() + "}", false);
    }

    @Test
    public void booleanContextValues() throws JSONException, CastleSdkConfigurationException {
        //given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();
        String contextJson = new CastleContextBuilder(configuration, model)
                .clientId(true)
                .userAgent(true)
                .active(false)
                .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"active\":false,\"client_id\":true,\"user_agent\":true," + SDKVersion.getLibraryString() + "}", false);
    }

    private String valueControlCache = "max-age=0";
    private String keyControlCache = "Cache-Control";
    private String userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";
    private String ip = "8.8.8.8";
    private String userAgentHeader = "User-Agent";
    private String customClientIdHeader = "X-Castle-Client-Id";
    private String acceptHeader = "Accept";
    private String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private String acceptLanguageHeader = "Accept-Language";
    private String acceptLanguage = "en-US,en;q=0.5";
    private String connectionHeader = "Connection";
    private String connection = "keep-alive";
    private String refererHeader = "Referer";
    private String referer = "http://localhost:8080/";
    private String headerHost = "Host";
    private String hostValue = "localhost:8080";
    private String acceptEncodingHeader = "Accept-Encoding";
    private String acceptEncoding = "gzip, deflate";
    private String cgiSpecHeaderName = "REMOTE_ADDR";

    public MockHttpServletRequest getStandardRequestMock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        request.addHeader(keyControlCache, valueControlCache);
        request.addHeader(userAgentHeader, userAgent);
//        request.addHeader(customClientIdHeader, clientId);
        request.addHeader(acceptHeader, accept);
        request.addHeader(acceptLanguageHeader, acceptLanguage);
        request.addHeader(acceptEncodingHeader, acceptEncoding);
        request.addHeader(headerHost, hostValue);
        request.addHeader(refererHeader, referer);
        request.addHeader(connectionHeader, connection);
        return request;
    }

    private List<CastleHeader> getStandardHeaders() {
        List<CastleHeader> listOfHeaders = new ArrayList<>();
        listOfHeaders.add(new CastleHeader(keyControlCache, valueControlCache));
        listOfHeaders.add(new CastleHeader(userAgentHeader, userAgent));
        listOfHeaders.add(new CastleHeader(acceptHeader, accept));
        listOfHeaders.add(new CastleHeader(acceptLanguageHeader, acceptLanguage));
        listOfHeaders.add(new CastleHeader(acceptEncodingHeader, acceptEncoding));
        listOfHeaders.add(new CastleHeader(headerHost, hostValue));
        listOfHeaders.add(new CastleHeader(refererHeader, referer));
        listOfHeaders.add(new CastleHeader(connectionHeader, connection));
        listOfHeaders.add(new CastleHeader(cgiSpecHeaderName, ip));
        return listOfHeaders;
    }

    public CastleHeaders getStandardCastleHeaders() {
        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(getStandardHeaders());
        return headers;
    }

    public CastleContext getStandardContext() {
        CastleContext expectedContext = new CastleContext();
        expectedContext.setHeaders(getStandardCastleHeaders());
        expectedContext.setUserAgent(userAgent);
        expectedContext.setDevice(getStandardDevice());
        expectedContext.setIp(ip);

        return expectedContext;
    }

    private CastleDevice getStandardDevice() {
        CastleDevice device = new CastleDevice();
        device.setId("d_id");
        device.setName("d_name");
        device.setType("d_type");
        device.setManufacturer("d_manufacturer");
        device.setModel("d_model");

        return device;
    }

    public CastleContext getStandardContextWithClientId(String clientId) {
        CastleContext expectedContext = getStandardContext();
        expectedContext.setClientId(clientId);
        List<CastleHeader> listOfHeaders = expectedContext.getHeaders().getHeaders();
        listOfHeaders.add(listOfHeaders.size()-1, new CastleHeader(customClientIdHeader, clientId));
        expectedContext.getHeaders().setHeaders(listOfHeaders);

        return expectedContext;
    }

    public CastleContext getStandardScrubbedContext() {
        CastleContext expectedContext = getStandardContext();

        List<CastleHeader> listOfHeaders = expectedContext.getHeaders().getHeaders();
        for (CastleHeader header : listOfHeaders) {
            header.setValue("true");
        }
        expectedContext.getHeaders().setHeaders(listOfHeaders);

        return expectedContext;
    }
}
