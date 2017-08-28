package io.castle.client;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.backend.DumbRestApiBackend;
import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class TestCastleWithDumbRestApiBackend {

    Castle sdk;
    DumbRestApiBackend dumbRestApiBackend;

    public TestCastleWithDumbRestApiBackend() throws CastleSdkConfigurationException {
    }

    @Before
    public void prepare() throws NoSuchFieldException, IllegalAccessException, CastleSdkConfigurationException {
        //Given a SDK instance
        sdk = new Castle(CastleSdkInternalConfiguration.getInternalConfiguration());
        RestApiFactory mockFactory = Mockito.mock(RestApiFactory.class);
        dumbRestApiBackend = new DumbRestApiBackend(sdk.getSdkConfiguration());
        Mockito.when(mockFactory.buildBackend())
                .thenReturn(dumbRestApiBackend);
        //When the utils are used to override the internal backend factory
        SdkMockUtil.modifyInternalBackendFactory(sdk, mockFactory);
    }


    @Test
    public void dumbAuthenticate() {
        //given
        String id = "1234";
        String event = "$login.succeeded";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();


        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id);

        // then the dumb backend should return the AuthenticateAction in the configuration, which is set from the properties file
        Assert.assertEquals(AuthenticateAction.CHALLENGE, authenticateAction);
    }


    @Test
    public void dumbAuthenticateWithProperties() {
        //given
        String id = "1234";
        String event = "$login.succeeded";
        HttpServletRequest request = new MockHttpServletRequest();

        ImmutableList<String> strings = ImmutableList.of("string1", "string2");

        // and an authenticate request is made
        AuthenticateAction authenticateAction = sdk.onRequest(request).authenticate(event, id, strings);
        // then the dumb backend should return the AuthenticateAction in the configuration
        Assert.assertEquals(AuthenticateAction.CHALLENGE, authenticateAction);
    }

    @Test
    public void dumbTrack() {
        //given
        String event = "$login.failed";
        HttpServletRequest request = new MockHttpServletRequest();

        //when
        sdk.onRequest(request).track(event);

        //no exception thrown
        // list of parameters added to dumb backend in order to test that events get passed correctly.
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains("$login.failed",
                        null,
                        null,
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}"
                );
    }

    @Test
    public void dumbTrackWithUserID() {
        //given
        String id = "1234";
        String event = "$login.failed";
        HttpServletRequest request = new MockHttpServletRequest();

        //when
        sdk.onRequest(request).track(event, id);

        //no exception thrown
        // list of parameters added to dumb backend in order to test that events get passed correctly.
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains("$login.failed",
                        id,
                        null,
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}"
                );
    }

    @Test
    public void dumbTrackWithUserIdAndProperties() {
        //given
        String id = "1234";
        String event = "$login.failed";
        HttpServletRequest request = new MockHttpServletRequest();
        ImmutableList<String> strings = ImmutableList.of("string1", "string2");

        //when
        sdk.onRequest(request).track(event, id, strings);

        //no exception thrown
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains("$login.failed",
                        id,
                        "[\"string1\",\"string2\"]",
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}"
                );
    }

    @Test
    public void dumbTrackWithDoNotTrackOn() {
        //given
        String id = "1234";
        String event = "$login.failed";
        HttpServletRequest request = new MockHttpServletRequest();
        ImmutableList<String> strings = ImmutableList.of("string1", "string2");

        //when
        sdk.onRequest(request)
                .doNotTrack(true)
                .track(event, id, strings);

        //then
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains(null,
                        "false");
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .doesNotContain(id,
                        "[\"string1\",\"string2\"]",
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}"
                );
    }

    @Test
    public void dumbCallWithMergedContext() throws CastleSdkConfigurationException {
        //given
        String event = "$login.failed";
        HttpServletRequest request = new MockHttpServletRequest();

        CastleContext castleContext = new CastleContext();
        castleContext.setClientId("test_client_id");

        //when
        sdk.onRequest(request).mergeContext(castleContext).track(event);

        //no exception thrown
        // TODO: order gets changed during merge
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains("$login.failed",
                        null,
                        null,
                        "{\"ip\":\"127.0.0.1\",\"headers\":{},\"client_id\":\"test_client_id\",\"active\":true,\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}"
                );
    }

    @Test
    public void dumbIdentifyCall() {
        //given
        String id = "1234";
        HttpServletRequest request = new MockHttpServletRequest();
        ImmutableList<String> traits = ImmutableList.of("string1", "string2");
        ImmutableList<String> properties = ImmutableList.of("string3", "string4");
        sdk.onRequest(request).identify(id, true, traits, properties);

        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains(null,
                        id,
                        "[\"string1\",\"string2\"]",
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}",
                        "[\"string3\",\"string4\"]",
                        "true"
                );
    }

    @Test
    public void dumbIdentifyCallWithNullPropertiesAndTraits() {
        //given
        String id = "1234";
        HttpServletRequest request = new MockHttpServletRequest();
        sdk.onRequest(request).identify(id, true, null, null);

        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains(null,
                        id,
                        null,
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}",
                        null,
                        "true"
                );
    }

    @Test
    public void dumbIdentifyWithDonotTrackOn() {
        //given
        String id = "1234";
        HttpServletRequest request = new MockHttpServletRequest();
        ImmutableList<String> traits = ImmutableList.of("string1", "string2");
        ImmutableList<String> properties = ImmutableList.of("string3", "string4");
        sdk.onRequest(request)
                .doNotTrack(true)
                .identify(id, true, traits, properties);

        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .contains(null,
                        "false");
        Assertions.assertThat(dumbRestApiBackend.getListOfParameters())
                .doesNotContain(id,
                        "[\"string1\",\"string2\"]",
                        "{\"active\":true,\"ip\":\"127.0.0.1\",\"headers\":{},\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}",
                        "[\"string3\",\"string4\"]"
                );
    }

}
