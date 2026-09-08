package io.castle.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import io.castle.client.internal.utils.OkHttpExceptionUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * Parsed HTTP response from the Castle API.
 * An empty or missing body is represented as JSON null.
 */
public class CastleResponse {
    private final int code;
    private final JsonElement json;

    public CastleResponse(Response response) throws IOException {
        OkHttpExceptionUtil.handle(response);

        code = response.code();
        ResponseBody responseBody = response.body();
        String body = responseBody != null ? responseBody.string() : "";
        json = body.isBlank() ? JsonNull.INSTANCE : JsonParser.parseString(body);
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public JsonElement json() {
        return json;
    }
}
