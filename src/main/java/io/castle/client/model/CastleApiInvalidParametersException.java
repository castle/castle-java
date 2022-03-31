package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInvalidParametersException extends CastleServerErrorException {
    public CastleApiInvalidParametersException(Response response) {
        super(response);
    }
}
