package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.utils.SDKVersion;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.collect.ImmutableMap;

import javax.servlet.http.HttpServletRequest;

public class CastleMergeHttpTest extends AbstractCastleHttpLayerTest {

    public CastleMergeHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void mergeContextIsSendOnHttp() throws InterruptedException, JSONException {

        //Given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();
        // And a extra context
        CustomExtraContext customExtraContext = new CustomExtraContext();
        customExtraContext.setAddString("String value");
        customExtraContext.setCondition(true);
        customExtraContext.setValue(10L);

        // and an authenticate request is made
        sdk.onRequest(request).mergeContext(customExtraContext).identify(id, ImmutableMap.builder()
                .put("x", "valueX")
                .put("y", 234567)
                .build());
        //When

        //Then the json send contains a extended context object
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();

        JSONAssert.assertEquals("{\"event\":\"any.valid.event\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +",\"add_string\":\"String value\",\"condition\":true,\"value\":10},\"user_traits\":{\"x\":\"valueX\",\"y\":234567}}", body, false);
    }

    @Test
    public void mergeContextIsNull() throws InterruptedException, JSONException {
        //Given
        server.enqueue(new MockResponse().setResponseCode(200));
        String id = "12345";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // and an identify request is made with a null context
        sdk.onRequest(request).mergeContext(null).identify(id);

        //Then the json send contains a context object with active key only
        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();
        JSONAssert.assertEquals("{\"user_id\":\"12345\",\"context\":{\"active\":true}}",
                body, false);
    }

    private static class CustomExtraContext {
        private String addString;
        private Boolean condition;
        private Long value;

        public String getAddString() {
            return addString;
        }

        public void setAddString(String addString) {
            this.addString = addString;
        }

        public Boolean getCondition() {
            return condition;
        }

        public void setCondition(Boolean condition) {
            this.condition = condition;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }
}
