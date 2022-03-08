package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInvalidRequestTokenException extends CastleApiInvalidParametersException {
    public CastleApiInvalidRequestTokenException(Response response) {
        super(response);
    }
}
