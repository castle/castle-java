package io.castle.client;

import com.sun.tools.internal.xjc.util.MimeTypeRange;
import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.internal.utils.Timestamp;
import io.castle.client.model.*;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CastleExceptionTest {

    private final MediaType JsonMediaType = MediaType.parse("application/json; charset=utf-8");

    @Test(expected = CastleApiInternalServerErrorException.class)
    public void serverError() {
        //Given
        Response response = new Response.Builder()
                .code(500)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test
    public void timeout() {
        //Given
        IOException exception = new SocketTimeoutException("timeout");

        CastleRuntimeException castleException = OkHttpExceptionUtil.handle(exception);

        Assert.assertTrue(castleException instanceof CastleApiTimeoutException);
    }

    @Test
    public void generic() {
        //Given
        IOException exception = new IOException("generic");

        CastleRuntimeException castleException = OkHttpExceptionUtil.handle(exception);

        Assert.assertFalse(castleException instanceof CastleApiTimeoutException);
        Assert.assertFalse(castleException instanceof CastleApiInternalServerErrorException);
    }

    @Test(expected = CastleApiInvalidRequestTokenErrorException.class)
    public void invalidRequestTokenError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create(JsonMediaType, "{\"type\": \"invalid_request_token\"}"))
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test(expected = CastleApiInvalidParametersErrorException.class)
    public void invalidParametersError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create(JsonMediaType, "{\"type\": \"unknown\"}"))
                .build();

        OkHttpExceptionUtil.handle(response);
    }
}
