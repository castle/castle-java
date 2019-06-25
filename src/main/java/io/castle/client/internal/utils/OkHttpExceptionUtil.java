package io.castle.client.internal.utils;

import io.castle.client.model.CastleApiInternalServerErrorException;
import io.castle.client.model.CastleApiTimeoutException;
import io.castle.client.model.CastleRuntimeException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class OkHttpExceptionUtil {
    public static CastleRuntimeException handle(IOException e) {
        if (e instanceof SocketTimeoutException) {
            return new CastleApiTimeoutException(e);
        } else if (e.getMessage().equals("HTTP request failure")) {
            return new CastleApiInternalServerErrorException(e);
        }
        return new CastleRuntimeException(e);
    }
}