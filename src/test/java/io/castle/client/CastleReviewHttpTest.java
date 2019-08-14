package io.castle.client;

import io.castle.client.model.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE;

public class CastleReviewHttpTest extends AbstractCastleHttpLayerTest {
    public CastleReviewHttpTest() {
        super(new AuthenticateFailoverStrategy());
    }

    @Test
    public void reviewEndpoint() throws InterruptedException {
        // given the mock server will response with a review json
        server.enqueue(new MockResponse().setBody(testReviewJson));
        HttpServletRequest request = new MockHttpServletRequest();
        String reviewId = " 123jkfg45f";

        // when sync review call is made
        Review loaded = sdk.onRequest(request).review(reviewId);

        // then the correct GET request is send
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/reviews/123jkfg45f"), recordedRequest.getRequestUrl());

        // and the correct Review model object is extracted
        Review expected = createExpectedReview();

        Assertions.assertThat(loaded).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void reviewAsyncEndpoint() throws InterruptedException {
        // given the mock server will response with a review json
        server.enqueue(new MockResponse().setBody(testReviewJson));
        HttpServletRequest request = new MockHttpServletRequest();
        String reviewId = " xcvknasdlfknlsdf";

        // when async review call is made
        final AtomicReference<Review> result = new AtomicReference<>();
        AsyncCallbackHandler<Review> callback = new AsyncCallbackHandler<Review>() {
            @Override
            public void onResponse(Review response) {
                result.set(response);
            }

            @Override
            public void onException(Exception exception) {
                Assertions.fail("error on request", exception);
            }
        };
        sdk.onRequest(request).reviewAsync(reviewId, callback);

        // then the correct GET request is send
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/reviews/xcvknasdlfknlsdf"), recordedRequest.getRequestUrl());

        // and the correct Review model object is extracted
        Review expected = createExpectedReview();
        Review loaded = waitForValue(result);
        Assertions.assertThat(loaded).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test(expected = CastleApiTimeoutException.class)
    public void reviewTimeoutTest() {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String reviewId = "mmnnsbkkkshhhs";

        //when a review request is made
        sdk.onRequest(request).review(reviewId);
    }

    @Test
    public void reviewAsyncTimeoutTest() throws Exception {

        // given backend request timeouts
        server.enqueue(new MockResponse().setSocketPolicy(NO_RESPONSE));
        HttpServletRequest request = new MockHttpServletRequest();
        String reviewId = "mmnnsbkkkshhhs";

        // when async review call is made
        final AtomicReference<Boolean> result = new AtomicReference<>();
        AsyncCallbackHandler<Review> callback = new AsyncCallbackHandler<Review>() {
            @Override
            public void onResponse(Review response) {
                Assertions.fail("should not pass");
            }

            @Override
            public void onException(Exception exception) {
                result.set(true);
            }
        };
        sdk.onRequest(request).reviewAsync(reviewId, callback);

        waitForValueAndVerify(result,true);
    }

    @Test(expected = CastleServerErrorException.class)
    public void testExceptionWithServerError () {

        // given a server failure
        server.enqueue(new MockResponse().setResponseCode(500));
        // And a request
        HttpServletRequest request = new MockHttpServletRequest();
        String reviewId = "mmnnsbkkkshhhs";

        //when a review request is made
        sdk.onRequest(request).review(reviewId);

    }

    String testReviewJson = "{\n" +
            "  \"user_id\": \"joH8NAVsYwRW4yBvPddS6TzydMmTjuzF\",\n" +
            "  \"context\": {\n" +
            "    \"ip\": \"69.181.35.4\",\n" +
            "    \"user_agent\": {\n" +
            "      \"raw\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) ...\",\n" +
            "      \"browser\": \"Chrome\",\n" +
            "      \"mobile\": false,\n" +
            "      \"os\": \"Mac OS X 10.9.5\",\n" +
            "      \"version\": \"42.0.2311\"\n" +
            "    },\n" +
            "    \"location\": {\n" +
            "      \"country\": \"United States\",\n" +
            "      \"country_code\": \"US\",\n" +
            "      \"region\": \"California\",\n" +
            "      \"region_code\": \"CA\",\n" +
            "      \"city\": \"San Francisco\",\n" +
            "      \"lon\": -55.654325,\n" +
            "      \"lat\": 13.043243\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private Review createExpectedReview() {
        Review review = new Review();
        review.setUserId("joH8NAVsYwRW4yBvPddS6TzydMmTjuzF");

        ReviewUserAgent expectedUserAgent = new ReviewUserAgent();
        expectedUserAgent.setRaw("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) ...");
        expectedUserAgent.setBrowser("Chrome");
        expectedUserAgent.setMobile(false);
        expectedUserAgent.setOs("Mac OS X 10.9.5");
        expectedUserAgent.setVersion("42.0.2311");

        ReviewLocation expectedLocation = new ReviewLocation();
        expectedLocation.setCity("San Francisco");
        expectedLocation.setCountry("United States");
        expectedLocation.setCountryCode("US");
        expectedLocation.setLat(13.043243d);
        expectedLocation.setLon(-55.654325d);
        expectedLocation.setRegion("California");
        expectedLocation.setRegionCode("CA");

        ReviewContext expectedContext = new ReviewContext();
        expectedContext.setIp("69.181.35.4");
        expectedContext.setUserAgent(expectedUserAgent);
        expectedContext.setLocation(expectedLocation);

        review.setContext(expectedContext);
        return review;
    }
}
