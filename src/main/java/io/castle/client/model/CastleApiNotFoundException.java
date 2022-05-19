package io.castle.client.model;

import io.castle.client.model.CastleServerErrorException;
import okhttp3.Response;

public class CastleApiNotFoundException extends CastleServerErrorException {
    public CastleApiNotFoundException(Response response) {
        super(response);
    }
}
