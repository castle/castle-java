package io.castle.client.model;

import okhttp3.Response;

import java.io.IOException;

public class CastleServerErrorException extends CastleRuntimeException {

    private final int responseCode;
    private final String responseMessage;
    private String response;

    public CastleServerErrorException(Response response) {
        super(response.toString());

        this.responseCode = response.code();
        this.responseMessage = response.message();
        try {
            this.response = response.body().string();
        } catch (NullPointerException | IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getResponse() {
        return response;
    }
}
