package io.castle.client.model;

import okhttp3.Response;

public class CastleServerErrorException extends CastleRuntimeException {

    private final int responseCode;
    private final String responseMessage;

    public CastleServerErrorException(Response response) {
        super(response.toString());

        this.responseCode = response.code();
        this.responseMessage = response.message();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
