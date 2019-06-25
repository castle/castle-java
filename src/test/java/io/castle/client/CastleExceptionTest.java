package io.castle.client;

import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.internal.utils.Timestamp;
import io.castle.client.model.CastleApiInternalServerErrorException;
import io.castle.client.model.CastleApiTimeoutException;
import io.castle.client.model.CastleRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CastleExceptionTest {

    @Test
    public void serverError() {
        //Given
        IOException exception = new IOException("HTTP request failure");

        CastleRuntimeException castleException = OkHttpExceptionUtil.handle(exception);

        Assert.assertTrue(castleException instanceof CastleApiInternalServerErrorException);
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

        Assert.assertTrue(!(castleException instanceof CastleApiTimeoutException));
        Assert.assertTrue(!(castleException instanceof CastleApiInternalServerErrorException));
    }
}
