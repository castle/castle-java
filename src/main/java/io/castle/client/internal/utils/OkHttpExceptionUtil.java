package io.castle.client.internal.utils;

import io.castle.client.model.CastleApiInternalServerErrorException;
import io.castle.client.model.CastleApiTimeoutException;
import io.castle.client.model.CastleRuntimeException;
import io.castle.client.model.CastleServerErrorException;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class OkHttpExceptionUtil {

    public static CastleRuntimeException handle(IOException e) {
        if (e instanceof SocketTimeoutException) {
            return new CastleApiTimeoutException(e);
        }
        return new CastleRuntimeException(e);
    }

    public static void handle(Response response) throws CastleServerErrorException {
        if (!response.isSuccessful() && !response.isRedirect()) {
            if (response.code() == 500) {
                throw new CastleApiInternalServerErrorException(response);
            }
            throw new CastleServerErrorException(response);
        }
    }
}
