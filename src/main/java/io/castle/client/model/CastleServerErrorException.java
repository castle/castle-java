package io.castle.client.model;

import okhttp3.Response;

public class CastleServerErrorException extends CastleRuntimeException {

    public CastleServerErrorException(Response response) {
        super(response.toString());
    }
}
