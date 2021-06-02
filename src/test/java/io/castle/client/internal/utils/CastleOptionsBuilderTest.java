package io.castle.client.internal.utils;

import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.config.CastleConfigurationBuilder;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.*;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CastleOptionsBuilderTest {

    private final CastleGsonModel model = new CastleGsonModel();

    @Test
    public void buildContextWithDefaultSetup() throws CastleSdkConfigurationException {

        //Given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();
        CastleOptions standardOptions = getStandardOptionsFromServletRequest();

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest)
                .build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void blockHeadersOnDenyList() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withDenyListHeaders("Cookie", acceptLanguageHeader)
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And expected castle options without the accept-language header
        CastleOptions standardOptions = getStandardOptionsFromServletRequest();
        List<CastleHeader> listOfHeaders = new ArrayList<>();
        for (CastleHeader header : standardOptions.getHeaders().getHeaders()) {
            if (!header.getKey().equals("Cookie") && !header.getKey().equals(acceptLanguageHeader)) {
                listOfHeaders.add(header);
            } else {
                listOfHeaders.add(new CastleHeader(header.getKey(), "true"));
            }
        }
        standardOptions.getHeaders().setHeaders(listOfHeaders);

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest).build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void allowListIsMoreRelevantThatDenyList() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withDenyListHeaders(connectionHeader)
                .withAllowListHeaders(connectionHeader)
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And expected castle options
        CastleOptions standardOptions = getStandardScrubbedOptions();
        List<CastleHeader> listOfHeaders = new ArrayList<>();
        for (CastleHeader header : standardOptions.getHeaders().getHeaders()) {
            if (!header.getKey().equals(connectionHeader)) {
                listOfHeaders.add(header);
            } else {
                listOfHeaders.add(new CastleHeader(header.getKey(), "true"));
            }
        }
        standardOptions.getHeaders().setHeaders(listOfHeaders);

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest)
                .build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void allowListPassHeadersToTheContext() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .withDefaultDenyList()
                .withAllowListHeaders(connectionHeader)
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);
        HttpServletRequest standardRequest = getStandardRequestMock();

        //And expected castle options with a single allowListed header
        CastleOptions standardOptions = getStandardScrubbedOptions();
        for (CastleHeader header : standardOptions.getHeaders().getHeaders()) {
            if (header.getKey().equals(connectionHeader)) {
                header.setValue(connection);
            }
        }

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest).build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void fingerprintIsInFirstPlaceTakenFromHeader() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);

        //And a http request with __cid cookie
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        Cookie cookie = new Cookie("__cid", "valueFromCookie");
        Cookie someOtherCookie = new Cookie("Some", "cookie");
        standardRequest.setCookies(cookie, someOtherCookie);
        //And a custom castle header
        standardRequest.addHeader(customClientIdHeader, "valueFromHeaders");

        //And expected options value with matching fingerprint
        CastleOptions standardOptions = getStandardOptionsWithFingerprint("valueFromHeaders");

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest)
                .build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void fingerprintUseFailoverValueFromHeaders() throws CastleSdkConfigurationException {

        //Given a Configuration that block the accept-language header
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("anyValidKey")
                .withCastleAppId("anyValidAppId")
                .build();
        CastleOptionsBuilder builder = new CastleOptionsBuilder(configuration, model);

        //And a http request without __cid cookie
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        //And a custom castle header
        standardRequest.addHeader(customClientIdHeader, "valueFromHeaders");

        //And an expected options value with matching fingerprint
        CastleOptions standardOptions = getStandardOptionsWithFingerprint("valueFromHeaders");

        //When
        CastleOptions options = builder.fromHttpServletRequest(standardRequest)
                .build();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void builderMethods() throws CastleSdkConfigurationException {
        // Given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();

        // When
        CastleOptions options = new CastleOptionsBuilder(configuration, model)
                .headers(getStandardCastleHeaders())
                .ip(ip)
                .build();

        // And
        CastleOptions standardOptions = getStandardOptions();

        //Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void toAndFromJson() throws CastleSdkConfigurationException {
        // Given
        MockHttpServletRequest standardRequest = getStandardRequestMock();
        CastleOptions standardOptions = getStandardOptionsFromServletRequest();
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();

        // When
        String json = new CastleOptionsBuilder(configuration, model)
                .fromHttpServletRequest(standardRequest)
                .toJson();

        CastleOptions options = new CastleOptionsBuilder(configuration, model)
                .fromJson(json)
                .build();

        // Then
        Assertions.assertThat(options).isEqualToComparingFieldByFieldRecursively(standardOptions);
    }

    @Test
    public void withManualHeaders() throws CastleSdkConfigurationException, JSONException {
        // Given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();
        String contextJson = new CastleOptionsBuilder(configuration, model)
                .headers(CastleHeaders.builder()
                        .add("User-Agent", "ua")
                        .build()
                )
                .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"fingerprint\":false,\"headers\":{\"User-Agent\":\"ua\"}}", false);
    }

    @Test
    public void booleanContextValues() throws JSONException, CastleSdkConfigurationException {
        //given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();
        String contextJson = new CastleOptionsBuilder(configuration, model)
                .fingerprint(true)
                .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"fingerprint\":true}", false);
    }

    @Test
    public void withCustomIPHeaders() throws CastleSdkConfigurationException, JSONException {
        // Given
        CastleConfiguration configuration = CastleConfigurationBuilder.defaultConfigBuilder()
                .apiSecret("abcd")
                .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
                .build();

        MockHttpServletRequest standardRequest = getStandardRequestMock();
        standardRequest.addHeader("X-Forwarded-For", "1.1.1.1,2.2.2.2,3.3.3.3");
        standardRequest.addHeader("CF-Connecting-IP", "4.4.4.4");

        CastleOptions standardOptions = getStandardOptions();
        standardOptions.setIp("1.1.1.1,2.2.2.2,3.3.3.3");

        CastleOptions options = new CastleOptionsBuilder(configuration, model)
                .fromHttpServletRequest(standardRequest)
                .build();

        //Then
        Assertions.assertThat(options.getIp()).isEqualTo(standardOptions.getIp());
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

    public CastleOptions getStandardOptions() {
        CastleOptions expectedOptions = new CastleOptions();
        expectedOptions.setHeaders(getStandardCastleHeaders());
        expectedOptions.setIp(ip);

        return expectedOptions;
    }

    public CastleOptions getStandardOptionsFromServletRequest() {
        CastleOptions expectedOptions = getStandardOptions();
        expectedOptions.setFingerprint(false);

        return expectedOptions;
    }

    public CastleOptions getStandardOptionsWithFingerprint(String fingerprint) {
        CastleOptions expectedOptions = getStandardOptions();
        expectedOptions.setFingerprint(fingerprint);
        List<CastleHeader> listOfHeaders = expectedOptions.getHeaders().getHeaders();
        listOfHeaders.add(listOfHeaders.size()-1, new CastleHeader(customClientIdHeader, fingerprint));
        expectedOptions.getHeaders().setHeaders(listOfHeaders);

        return expectedOptions;
    }

    public CastleOptions getStandardScrubbedOptions() {
        CastleOptions expectedOptions = getStandardOptionsFromServletRequest();

        List<CastleHeader> listOfHeaders = expectedOptions.getHeaders().getHeaders();
        for (CastleHeader header : listOfHeaders) {
            header.setValue("true");
        }
        expectedOptions.getHeaders().setHeaders(listOfHeaders);

        return expectedOptions;
    }
}