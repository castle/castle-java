package io.castle.client.internal.backend;

import com.google.gson.*;
import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.AsyncCallbackHandler;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateResponse;
import io.castle.client.internal.json.CastleGsonModel;
import okhttp3.*;

import java.io.IOException;

public class OkRestApiBackend implements RestApi {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private final HttpUrl track;
    private final HttpUrl authenticate;

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel modelInstance, CastleConfiguration configuration) {
        this(client, modelInstance, configuration, HttpUrl.parse("https://api.castle.io/"));
    }

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel model, CastleConfiguration configuration, HttpUrl baseUrl) {
        this.client = client;
        this.model = model;
        this.configuration = configuration;
        this.track = baseUrl.resolve("/v1/track.json");
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
                //TODO should we care of failing track events?
                if (!response.isSuccessful()) {
                    Castle.logger.info("HTTP layer. Track event not delivered.");
                }
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
    public AuthenticateAction sendAuthenticateSync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload) {
        Request request = buildAuthenticateRequest(event, userId, contextPayload, propertiesPayload);
        try {
            Response response = client.newCall(request).execute();
            return extractAuthenticationAction(response);
        } catch (IOException e) {
            Castle.logger.error("HTTP layer. Error sending request.", e);
        }
        return configuration.getAuthenticateFailoverStrategy().getDefaultAction();
    }

    @Override
    public void sendAuthenticateAsync(String event, String userId, JsonElement contextPayload, JsonElement propertiesPayload, final AsyncCallbackHandler<AuthenticateAction> asyncCallbackHandler) {

        Request request = buildAuthenticateRequest(event, userId, contextPayload, propertiesPayload);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                asyncCallbackHandler.onResponse(configuration.getAuthenticateFailoverStrategy().getDefaultAction());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                asyncCallbackHandler.onResponse(extractAuthenticationAction(response));
            }
        });

    }

    private AuthenticateAction extractAuthenticationAction(Response response) throws IOException {
        AuthenticateAction authenticateAction;
        if (response.isSuccessful()) {
            String jsonResponse = response.body().string();
            Gson gson = model.getGson();
            AuthenticateResponse authenticateResponse = gson.fromJson(jsonResponse, AuthenticateResponse.class);
            String action = authenticateResponse.getAction();
            authenticateAction = AuthenticateAction.fromAction(action);
        } else {
            authenticateAction = configuration.getAuthenticateFailoverStrategy().getDefaultAction();
        }
        return authenticateAction;
    }

    @Override
    public void sendIdentifyRequest(String userId, JsonObject contextPayload, boolean active, JsonElement traitsJson, JsonElement propertiesPayload) {
        JsonObject json = new JsonObject();
        json.add("user_id", new JsonPrimitive(userId));
        json.add("context", contextPayload);
        if (traitsJson != null) {
            json.add("traits", traitsJson);
        }
        if (propertiesPayload != null) {
            json.add("properties", propertiesPayload);
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
