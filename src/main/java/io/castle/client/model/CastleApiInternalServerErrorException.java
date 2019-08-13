package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInternalServerErrorException extends CastleServerErrorException {

    public CastleApiInternalServerErrorException(Response response) {
        super(response);
    }
}
