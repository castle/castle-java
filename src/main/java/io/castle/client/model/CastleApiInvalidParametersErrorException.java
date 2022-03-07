package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInvalidParametersErrorException extends CastleServerErrorException {
    public CastleApiInvalidParametersErrorException(Response response) {
        super(response);
    }
}
