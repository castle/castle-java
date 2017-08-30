package io.castle.client.internal.backend;

import com.google.gson.*;
import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.CastleRuntimeException;
import io.castle.client.model.Verdict;
import okhttp3.*;

import java.io.IOException;

public class OkRestApiBackend implements RestApi {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private final HttpUrl track;
    private final HttpUrl authenticate;

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel model, CastleConfiguration configuration) {
        HttpUrl baseUrl = HttpUrl.parse(configuration.getApiBaseUrl());
        this.client = client;
        this.model = model;
        this.configuration = configuration;
        this.track = baseUrl.resolve("/v1/track");
        this.authenticate = baseUrl.resolve("/v1/authenticate");
    }

    @Override
    public void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        sendTrackRequest(event, userId, contextPayload, propertiesPayload, null);
    }

    @Override
    public void sendTrackRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, final AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        JsonObject json = new JsonObject();
        json.add("name", new JsonPrimitive(event));
        if (userId == null) {
            json.add("user_id", JsonNull.INSTANCE);
        } else {
            json.add("user_id", new JsonPrimitive(userId));
        }
        json.add("context", contextPayload);
        if (propertiesPayload != null) {
            json.add("properties", propertiesPayload);
        }
        String content = json.toString();
        RequestBody body = RequestBody.create(JSON, content);
        Request request = new Request.Builder()
                .url(track)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Castle.logger.error("HTTP layer. Error sending track request.", e);
                if (asyncCallbackHandler != null) {
                    asyncCallbackHandler.onResponse(false);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (asyncCallbackHandler != null) {
                    asyncCallbackHandler.onResponse(response.isSuccessful());
                }
            }
        });
    }

    private Request buildAuthenticateRequest(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        JsonObject json = new JsonObject();
        json.add("name", new JsonPrimitive(event));
        json.add("user_id", new JsonPrimitive(userId));
        json.add("context", contextPayload);
        if (propertiesPayload != null) {
            json.add("properties", propertiesPayload);
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        return new Request.Builder()
                .url(authenticate)
                .post(body)
                .build();
    }

    @Override
    public Verdict sendAuthenticateSync(String event, final String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        Request request = buildAuthenticateRequest(event, userId, contextPayload, propertiesPayload);
        try {
            Response response = client.newCall(request).execute();
            return extractAuthenticationAction(response, userId);
        } catch (IOException e) {
            Castle.logger.error("HTTP layer. Error sending request.", e);
            if (configuration.getAuthenticateFailoverStrategy().isThrowTimeoutException()) {
                throw new CastleRuntimeException(e);
            } else {
                Verdict verdict = new Verdict();
                verdict.setUserId(userId);
                verdict.setAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction());
                return verdict;
            }
        }
    }

    @Override
    public void sendAuthenticateAsync(String event, final String userId, JsonElement contextPayload, JsonElement propertiesPayload, final AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        Request request = buildAuthenticateRequest(event, userId, contextPayload, propertiesPayload);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (configuration.getAuthenticateFailoverStrategy().isThrowTimeoutException()) {
                    asyncCallbackHandler.onException(e);
                } else {
                    // TODO common method call
                    Verdict verdict = new Verdict();
                    verdict.setUserId(userId);
                    verdict.setAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction());
                    asyncCallbackHandler.onResponse(verdict);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                asyncCallbackHandler.onResponse(extractAuthenticationAction(response, userId));
            }
        });

    }

    private Verdict extractAuthenticationAction(Response response, String userId) throws IOException {
        if (response.isSuccessful()) {
            String jsonResponse = response.body().string();
            Gson gson = model.getGson();
            Verdict verdict = gson.fromJson(jsonResponse, Verdict.class);
            if (verdict.getAction() != null) {
                return verdict;
            }
        }
        Verdict verdict = new Verdict();
        verdict.setUserId(userId);
        verdict.setAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction());
        return verdict;
    }

    @Override
    public void sendIdentifyRequest(String userId, JsonObject contextJson, boolean active, JsonElement traitsJson) {
        JsonObject json = new JsonObject();
        json.add("user_id", new JsonPrimitive(userId));
        json.add("active", new JsonPrimitive(active));
        json.add("context", contextJson);
        if (traitsJson != null) {
            json.add("traits", traitsJson);
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(authenticate)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Castle.logger.error("HTTP layer. Error sending request.", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Castle.logger.debug("Identify request successful");
            }
        });

    }
}
