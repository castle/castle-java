package io.castle.client.model;

import okhttp3.Response;

public class CastleApiInvalidRequestTokenErrorException extends CastleApiInvalidParametersErrorException {
    public CastleApiInvalidRequestTokenErrorException(Response response) {
        super(response);
    }
}
