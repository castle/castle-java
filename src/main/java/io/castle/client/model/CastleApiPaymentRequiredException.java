package io.castle.client.model;

import okhttp3.Response;

public class CastleApiPaymentRequiredException extends CastleServerErrorException {
    public CastleApiPaymentRequiredException(Response response) {
        super(response);
    }
}
