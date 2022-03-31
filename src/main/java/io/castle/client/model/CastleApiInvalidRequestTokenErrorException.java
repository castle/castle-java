package io.castle.client.model;

import okhttp3.Response;

/**
 * @deprecated
 * Use {@link CastleApiInvalidRequestTokenException} instead.
 */
@Deprecated
public class CastleApiInvalidRequestTokenErrorException extends CastleApiInvalidRequestTokenException {
    public CastleApiInvalidRequestTokenErrorException(Response response) {
        super(response);
    }
}
