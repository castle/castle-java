package io.castle.client.internal.backend;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.internal.utils.VerdictBuilder;
import io.castle.client.internal.utils.VerdictTransportModel;
import io.castle.client.model.*;
import okhttp3.*;

import java.io.IOException;

public class OkRestApiBackend implements RestApi {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    private final HttpUrl baseUrl;
    private final HttpUrl track;
    private final HttpUrl authenticate;
    private final HttpUrl deviceBase;
    private final HttpUrl userBase;
    private final HttpUrl impersonateBase;
    private final HttpUrl privacyBase;

    public OkRestApiBackend(OkHttpClient client, CastleGsonModel model, CastleConfiguration configuration) {
        this.baseUrl = HttpUrl.parse(configuration.getApiBaseUrl());
        this.client = client;
        this.model = model;
        this.configuration = configuration;
        this.track = baseUrl.resolve(Castle.URL_TRACK);
        this.authenticate = baseUrl.resolve(Castle.URL_AUTHENTICATE);
        this.deviceBase = baseUrl.resolve(Castle.URL_DEVICES);
        this.userBase = baseUrl.resolve(Castle.URL_USERS);
        this.impersonateBase = baseUrl.resolve(Castle.URL_IMPERSONATE);
        this.privacyBase = baseUrl.resolve(Castle.URL_PRIVACY);
    }

    @Override
    public void sendTrackRequest(JsonElement payload, final AsyncCallbackHandler<Boolean> asyncCallbackHandler) {
        RequestBody body = buildRequestBody(payload);
        Request request = new Request.Builder()
                .url(track)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Castle.logger.error("HTTP layer. Error sending track request.", e);
                if (asyncCallbackHandler != null) {
                    asyncCallbackHandler.onException(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (asyncCallbackHandler != null) {
                        asyncCallbackHandler.onResponse(response.isSuccessful());
                    }
                }
            }
        });
    }

    @Override
    public Verdict sendAuthenticateSync(JsonElement payloadJson) {
        final String userId = getUserIdFromPayload(payloadJson);

        RequestBody body = buildRequestBody(payloadJson);
        Request request = new Request.Builder()
                .url(authenticate)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return extractAuthenticationAction(response, userId);
        } catch (IOException e) {
            Castle.logger.error("HTTP layer. Error sending request.", e);
            if (configuration.getAuthenticateFailoverStrategy().isThrowTimeoutException()) {
                throw OkHttpExceptionUtil.handle(e);
            } else {
                return VerdictBuilder.failover(e.getMessage())
                        .withAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction())
                        .withUserId(userId)
                        .build();
            }
        }
    }

    @Override
    public void sendAuthenticateAsync(JsonElement payloadJson, final AsyncCallbackHandler<Verdict> asyncCallbackHandler) {
        final String userId = getUserIdFromPayload(payloadJson);

        RequestBody body = buildRequestBody(payloadJson);
        Request request = new Request.Builder()
                .url(authenticate)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (configuration.getAuthenticateFailoverStrategy().isThrowTimeoutException()) {
                    asyncCallbackHandler.onException(OkHttpExceptionUtil.handle(e));
                } else {
                    asyncCallbackHandler.onResponse(
                            VerdictBuilder.failover(e.getMessage())
                                    .withAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction())
                                    .withUserId(userId)
                                    .build()
                    );
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    asyncCallbackHandler.onResponse(extractAuthenticationAction(response, userId));
                }
            }
        });
    }

    private String getUserIdFromPayload(JsonElement payloadJson) {
        final String userId = ((JsonObject) payloadJson).has("user_id") ? ((JsonObject) payloadJson).get("user_id").getAsString() : null;
        if (userId == null) {
            Castle.logger.warn("Authenticate called with user_id null. Is this correct?");
        }
        return userId;
    }

    private RequestBody buildRequestBody(JsonElement payloadJson) {
        JsonObject json = payloadJson.getAsJsonObject();
        return RequestBody.create(JSON, json.toString());
    }

    private Verdict extractAuthenticationAction(Response response, String userId) throws IOException {
        String errorReason = response.message();
        String jsonResponse = response.body().string();

        if (response.isSuccessful()) {
            Gson gson = model.getGson();
            JsonParser jsonParser = model.getJsonParser();
            VerdictTransportModel transport = gson.fromJson(jsonResponse, VerdictTransportModel.class);
            if (transport != null && transport.getAction() != null) {
                return VerdictBuilder.fromTransport(transport, jsonParser.parse(jsonResponse));
            } else {
                errorReason = "Invalid JSON in response";
            }
        }

        if (response.code() >= 500) {
            //Use failover for error backends calls.
            if (!configuration.getAuthenticateFailoverStrategy().isThrowTimeoutException()) {
                Verdict verdict = VerdictBuilder.failover(errorReason)
                        .withAction(configuration.getAuthenticateFailoverStrategy().getDefaultAction())
                        .withUserId(userId)
                        .build();
                return verdict;
            } else {
                throw new CastleApiInternalServerErrorException(response);
            }
        }

        // Could not extract Verdict, so fail for client logic space.
        throw new CastleRuntimeException(response);
    }

    @Override
    public Boolean sendPrivacyRemoveUser(String userId) {
        Request request = createPrivacyRemoveRequest(userId);
        try (Response response = client.newCall(request).execute()) {
            return extractResponse(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleUserDevice sendApproveDeviceRequestSync(String deviceToken) {
        Request request = createApproveDeviceRequest(deviceToken);
        try (Response response = client.newCall(request).execute()) {
            return extractDevice(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleUserDevice sendReportDeviceRequestSync(String deviceToken) {
        Request request = createReportDeviceRequest(deviceToken);
        try (Response response = client.newCall(request).execute()) {
            return extractDevice(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleUserDevices sendGetUserDevicesRequestSync(String userId) {
        Request request = createGetUserDevicesRequest(userId);
        try (Response response = client.newCall(request).execute()) {
            return extractDevices(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleUserDevice sendGetUserDeviceRequestSync(String deviceToken) {
        Request request = createGetUserDeviceRequest(deviceToken);
        try (Response response = client.newCall(request).execute()) {
            return extractDevice(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleSuccess sendImpersonateStartRequestSync(String userId, String impersonator, JsonObject contextJson) {
        Request request = createImpersonateStartRequest(userId, impersonator, contextJson);
        try (Response response = client.newCall(request).execute()) {
            return extractSuccess(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    @Override
    public CastleSuccess sendImpersonateEndRequestSync(String userId, String impersonator, JsonObject contextJson) {
        Request request = createImpersonateEndRequest(userId, impersonator, contextJson);
        try (Response response = client.newCall(request).execute()) {
            return extractSuccess(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    public JsonElement get(String url) {
        return makeRequest(url, null, "GET");
    }

    @Override
    public JsonElement put(String url) {
        return makeRequest(url, null, "PUT");
    }

    @Override
    public JsonElement put(String url, ImmutableMap<String, Object> payload) {
        return makeRequest(url, model.getGson().toJsonTree(payload), "PUT");
    }

    @Override
    public JsonElement delete(String url) {
        return makeRequest(url, null, "DELETE");
    }

    @Override
    public JsonElement delete(String url, ImmutableMap<String, Object> payload) {
        return makeRequest(url, model.getGson().toJsonTree(payload), "DELETE");
    }

    @Override
    public JsonElement post(String url, ImmutableMap<String, Object> payload) {
        return makeRequest(url, model.getGson().toJsonTree(payload), "POST");
    }

    private JsonElement makeRequest(String url, JsonElement payload, String method) {
        RequestBody body = payload != null ? RequestBody.create(JSON, payload.toString()) : createEmptyRequestBody();

        Request.Builder builder = new Request.Builder()
                .url(baseUrl.resolve(url));

        switch (method) {
            case "DELETE":
                builder.delete(body);
                break;
            case "POST":
                builder.post(body);
                break;
            case "PUT":
                builder.put(body);
                break;
            case "GET":
                builder.get();
                break;
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return extractJsonElement(response);
        } catch (IOException e) {
            throw OkHttpExceptionUtil.handle(e);
        }
    }

    private JsonElement extractJsonElement(Response response) throws IOException {
        if (response.isSuccessful()) {
            String jsonResponse = response.body().string();
            JsonParser gson = new JsonParser();
            return gson.parse(jsonResponse);
        } else if (response.code() == 404) {
            return null;
        }
        OkHttpExceptionUtil.handle(response);
        return null;
    }

    private CastleUserDevice extractDevice(Response response) throws IOException {
        return (CastleUserDevice) extract(response, CastleUserDevice.class);
    }

    private CastleUserDevices extractDevices(Response response) throws IOException {
        return (CastleUserDevices) extract(response, CastleUserDevices.class);
    }

    private Object extract(Response response, Class clazz) throws IOException {
        if (response.isSuccessful()) {
            String jsonResponse = response.body().string();
            Gson gson = model.getGson();
            return gson.fromJson(jsonResponse, clazz);
        } else if (response.code() == 404) {
            return null;
        }
        OkHttpExceptionUtil.handle(response);
        return null;
    }

    private CastleSuccess extractSuccess(Response response) throws IOException {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                String jsonResponse = response.body().string();
                Gson gson = model.getGson();
                return gson.fromJson(jsonResponse, CastleSuccess.class);
            }
        }
        OkHttpExceptionUtil.handle(response);
        return null;
    }

    private Boolean extractResponse(Response response) {
        if (response.isSuccessful()) {
            return true;
        } else if (response.code() == 404) {
            return null;
        }
        OkHttpExceptionUtil.handle(response);
        return false;
    }

    private CastleUser extractUser(Response response) throws IOException {
        return (CastleUser) extract(response, CastleUser.class);
    }

    private Request createApproveDeviceRequest(String deviceToken) {
        HttpUrl approveDeviceUrl = deviceBase.resolve(deviceToken + "/approve");
        return new Request.Builder()
                .url(approveDeviceUrl)
                .put(createEmptyRequestBody())
                .build();
    }

    private Request createReportDeviceRequest(String deviceToken) {
        HttpUrl reportDeviceUrl = deviceBase.resolve(deviceToken + "/report");
        return new Request.Builder()
                .url(reportDeviceUrl)
                .put(createEmptyRequestBody())
                .build();
    }

    private Request createGetUserDevicesRequest(String userId) {
        HttpUrl getUserDevicesUrl = userBase.resolve(userId + "/devices");
        return new Request.Builder()
                .url(getUserDevicesUrl)
                .get()
                .build();
    }

    private Request createGetUserDeviceRequest(String deviceToken) {
        HttpUrl getUserDeviceUrl = deviceBase.resolve(deviceToken);
        return new Request.Builder()
                .url(getUserDeviceUrl)
                .get()
                .build();
    }

    private Request createImpersonateStartRequest(String userId, String impersonator, JsonObject contextJson) {
        HttpUrl impersonateUrl = impersonateBase;

        ImpersonatePayload payload = new ImpersonatePayload(userId, impersonator, contextJson);

        RequestBody body = RequestBody.create(JSON, model.getGson().toJson(payload));

        return new Request.Builder()
                .url(impersonateUrl)
                .post(body)
                .build();
    }

    private Request createImpersonateEndRequest(String userId, String impersonator, JsonObject contextJson) {
        HttpUrl impersonateUrl = impersonateBase;

        ImpersonatePayload payload = new ImpersonatePayload(userId, impersonator, contextJson);

        RequestBody body = RequestBody.create(JSON, model.getGson().toJson(payload));

        return new Request.Builder()
                .url(impersonateUrl)
                .delete(body)
                .build();
    }

    private Request createPrivacyRemoveRequest(String userId) {
        HttpUrl privacyRemoveUrl = privacyBase.resolve("users/" + userId);
        return new Request.Builder()
                .url(privacyRemoveUrl)
                .delete()
                .build();
    }

    private RequestBody createEmptyRequestBody() {
        return RequestBody.create(null, new byte[0]);
    }
}
