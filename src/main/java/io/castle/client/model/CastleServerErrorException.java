package io.castle.client.model;

import okhttp3.Response;

public class CastleServerErrorException extends CastleRuntimeException {

    public CastleServerErrorException(Throwable throwable) {
        super(throwable);
    }

    public CastleServerErrorException(String message) {
        super(message);
    }

    public CastleServerErrorException(Response response) {
        super(response.toString());
    }
}
