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
import java.util.Arrays;
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
        CastleContext standardContext = getStandardContextFromServletRequest();

        //When
        CastleContext context = builder
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
        CastleContext standardContext = getStandardContextFromServletRequest();
        CastleConfiguration configuration = CastleConfigurationBuilder
            .defaultConfigBuilder()
            .withApiSecret("abcd")
            .build();

        // When
        String json = new CastleContextBuilder(configuration, model)
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
            .device(getStandardDevice())
            .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"active\":true,\"fingerprint\":false,\"user_agent\":\"ua\",\"device\":{\"id\":\"d_id\",\"manufacturer\":\"d_manufacturer\",\"model\":\"d_model\",\"name\":\"d_name\",\"type\":\"d_type\"},\"headers\":{\"User-Agent\":\"ua\"}," + SDKVersion.getLibraryString() + "}", false);
    }

    @Test
    public void booleanContextValues() throws JSONException, CastleSdkConfigurationException {
        //given
        CastleConfiguration configuration = CastleConfigurationBuilder
                .defaultConfigBuilder()
                .withApiSecret("abcd")
                .build();
        String contextJson = new CastleContextBuilder(configuration, model)
                .active(false)
                .toJson();

        // Then
        JSONAssert.assertEquals(contextJson, "{\"active\":false," + SDKVersion.getLibraryString() + "}", false);
    }

    private String valueControlCache = "max-age=0";
    private String keyControlCache = "Cache-Control";
    private String userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";
    private String ip = "8.8.8.8";
    private String userAgentHeader = "User-Agent";
    private String customFingerprintHeader = "X-Castle-Client-Id";
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
        expectedContext.setDevice(getStandardDevice());

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

    public CastleContext getStandardContextFromServletRequest() {
        CastleContext expectedContext = getStandardContext();

        return expectedContext;
    }
}
