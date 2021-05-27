package io.castle.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.castle.client.internal.utils.OkHttpExceptionUtil;
import okhttp3.Response;

import java.io.IOException;

public class CastleResponse {
    private final int code;
    private final JsonElement json;

    public CastleResponse(Response response) throws IOException {
        code = response.code();
        String body = response.body().string();
        JsonParser gson = new JsonParser();
        json = gson.parse(body);

        OkHttpExceptionUtil.handle(response);
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public JsonElement json() {
        return json;
    }
}
