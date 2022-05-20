package io.castle.client.model;

import io.castle.client.Castle;
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
            Castle.logger.error("CastleServerErrorException. No response body.", e);
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

    @Override
    public String getMessage() {
        if (this.response != null) {
            return super.getMessage() + " body: " + this.response;
        }
        return super.getMessage();
    }
}
