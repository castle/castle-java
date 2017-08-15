package io.castle.client.internal.backend;

import com.google.gson.Gson;
import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.model.AuthenticateAction;
import io.castle.client.internal.model.AuthenticateResponse;
import io.castle.client.internal.model.json.CastleGsonModel;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.Future;

//TODO handle timeout case
public class OkRestApiBackend implements RestApi {

    private final OkHttpClient client;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private final HttpUrl baseUrl;
    private final HttpUrl track;
    private final HttpUrl authenticate;

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel modelInstance, CastleConfiguration configuration) {
        this.model = modelInstance;
        this.client = client;
        this.configuration = configuration;
        this.baseUrl = HttpUrl.parse("https://api.castle.io/");
        this.track = baseUrl.resolve("/v1/track.json");
        this.authenticate = baseUrl.resolve("/v1/authenticate");
    }

    @Override
    public int sendTrackRequest(String event, String userId, String contextPayload, String propertiesPayload) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", event)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("context", contextPayload);
        if (propertiesPayload != null) {
            builder.addFormDataPart("properties", propertiesPayload);
        }
        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .url(track)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //TODO should we case of failing track events?
            if (!response.isSuccessful()) {
                Castle.logger.info("HTTP layer. Track event not delivered.");
            }
            return response.code();
        } catch (IOException e) {
            Castle.logger.error("HTTP layer. Error sending request.", e);
        }
        return -1;
    }

    @Override
    public AuthenticateAction sendAuthenticateSync(String event, String userId, String contextPayload, String propertiesPayload) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", event)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("context", contextPayload);
        if (propertiesPayload != null) {
            builder.addFormDataPart("properties", propertiesPayload);
        }
        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .url(authenticate)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                Gson gson = model.getGson();
                AuthenticateResponse authenticateResponse = gson.fromJson(jsonResponse, AuthenticateResponse.class);
                //TODO Is userID necessary here?
                String action = authenticateResponse.getAction();
                return AuthenticateAction.fromAction(action);
            }
        } catch (IOException e) {
            Castle.logger.error("HTTP layer. Error sending request.", e);
        }
        return configuration.getFailoverStrategy().getDefaultAction();
    }

    @Override
    public Future<AuthenticateAction> sendAuthenticateAsync(String payload) {
        return null;
    }


}
