package io.castle.client.model;

public class CastleApiInternalServerErrorException extends CastleRuntimeException {

    public CastleApiInternalServerErrorException(Throwable throwable) {
        super(throwable);
    }

    public CastleApiInternalServerErrorException(String message) {
        super(message);
    }
}
