package io.castle.client.model;

import okhttp3.Response;

/**
 * @deprecated
 * Use {@link CastleApiInvalidParametersException} instead.
 */
@Deprecated
public class CastleApiInvalidParametersErrorException extends CastleApiInvalidParametersException {
    public CastleApiInvalidParametersErrorException(Response response) {
        super(response);
    }
}
