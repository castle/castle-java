package io.castle.client;

import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.internal.utils.Timestamp;
import io.castle.client.model.CastleApiInternalServerErrorException;
import io.castle.client.model.CastleApiTimeoutException;
import io.castle.client.model.CastleRuntimeException;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
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
}
