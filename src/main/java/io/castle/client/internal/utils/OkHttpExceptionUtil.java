package io.castle.client.internal.utils;

import com.google.gson.*;
import io.castle.client.model.*;
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
            if (response.code() == 422) {
                try {
                    String body = response.peekBody(Long.MAX_VALUE).string();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject json = (JsonObject) jsonParser.parse(body);
                    String type = json.get("type").getAsString();

                    if (type.equals("invalid_request_token")) {
                        throw new CastleApiInvalidRequestTokenErrorException(response);
                    }
                } catch (IOException | JsonSyntaxException |JsonIOException ignored) {}
                throw new CastleApiInvalidParametersErrorException(response);
            }
            throw new CastleServerErrorException(response);
        }
    }
}
