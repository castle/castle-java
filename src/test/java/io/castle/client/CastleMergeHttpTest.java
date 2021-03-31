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
import java.util.concurrent.atomic.AtomicReference;
import io.castle.client.model.AsyncCallbackHandler;
import org.assertj.core.api.Assertions;

public class CastleMergeHttpTest extends AbstractCastleHttpLayerTest {

    public CastleMergeHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void mergeContextIsSendOnHttp() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setResponseCode(200));
        String event = "any.valid.event";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And a extra context
        CustomExtraContext customExtraContext = new CustomExtraContext();
        customExtraContext.setAddString("String value");
        customExtraContext.setCondition(true);
        customExtraContext.setValue(10L);
        sdk.onRequest(request).mergeContext(customExtraContext).track(event, null, null, null, ImmutableMap.builder()
            .put("x", "valueX")
            .put("y", 234567)
            .build());

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();

        JSONAssert.assertEquals("{\"event\":\"any.valid.event\",\"context\":{\"active\":true," + SDKVersion.getLibraryString() +",\"add_string\":\"String value\",\"condition\":true,\"value\":10},\"user_traits\":{\"x\":\"valueX\",\"y\":234567}}", body, false);
    }

    @Test
    public void mergeContextIsNull() throws InterruptedException, JSONException {
        // Given
        server.enqueue(new MockResponse().setResponseCode(200));
        String event = "any.valid.event";

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        // And null context
        sdk.onRequest(request).mergeContext(null).track(event, null, null, null);

        RecordedRequest recordedRequest = server.takeRequest();
        String body = recordedRequest.getBody().readUtf8();

        JSONAssert.assertEquals("{\"event\":\"any.valid.event\"}", body, false);
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
