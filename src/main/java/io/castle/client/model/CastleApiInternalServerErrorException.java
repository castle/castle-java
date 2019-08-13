package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInternalServerErrorException extends CastleServerErrorException {

    public CastleApiInternalServerErrorException(Throwable throwable) {
        super(throwable);
    }

    public CastleApiInternalServerErrorException(String message) {
        super(message);
    }

    public CastleApiInternalServerErrorException(Response response) {
        super(response);
    }
}
