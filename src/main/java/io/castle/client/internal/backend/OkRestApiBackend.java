package io.castle.client.internal.backend;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.model.*;
import okhttp3.*;

import java.io.IOException;

public class OkRestApiBackend implements RestApi {

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_GET = "GET";

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final CastleGsonModel model;

    private final HttpUrl baseUrl;

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel model, CastleConfiguration configuration) {
        this.baseUrl = HttpUrl.parse(configuration.getApiBaseUrl());
        this.client = client;
        this.model = model;
    }

    public CastleResponse get(String path) {
        return makeRequest(path, null, METHOD_GET);
    }

    @Override
    public CastleResponse put(String path) {
        return makeRequest(path, null, METHOD_PUT);
    }

    @Override
    public CastleResponse put(String path, ImmutableMap<Object, Object> payload) {
        return makeRequest(path, model.getGson().toJsonTree(payload), METHOD_PUT);
    }

    @Override
    public CastleResponse delete(String path) {
        return makeRequest(path, null, METHOD_DELETE);
    }

    @Override
    public CastleResponse delete(String path, ImmutableMap<Object, Object> payload) {
        return makeRequest(path, model.getGson().toJsonTree(payload), METHOD_DELETE);
    }

    @Override
    public CastleResponse post(String path, ImmutableMap<Object, Object> payload) {
        return makeRequest(path, model.getGson().toJsonTree(payload), METHOD_POST);
    }

    private CastleResponse makeRequest(String path, JsonElement payload, String method) {
        RequestBody body = payload != null ? RequestBody.create(JSON, payload.toString()) : createEmptyRequestBody();

        Request.Builder builder = new Request.Builder()
                .url(baseUrl.resolve(path));

        switch (method) {
            case METHOD_DELETE:
                builder.delete(body);
                break;
            case METHOD_POST:
                builder.post(body);
                break;
            case METHOD_PUT:
                builder.put(body);
                break;
            case METHOD_GET:
                builder.get();
                break;
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return new CastleResponse(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    private RequestBody createEmptyRequestBody() {
        return RequestBody.create(null, new byte[0]);
    }
}
