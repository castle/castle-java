package io.castle.client.exceptions;

import io.castle.client.objects.Error;

import java.net.URI;

public class CastleException extends RuntimeException {

    private static final long serialVersionUID = 2445885434687308945L;

    private int responseCode = -1;
    private io.castle.client.objects.Error error;
    private URI uri;
    
    public CastleException(String message) {
	super(message);
    }
    
    public CastleException(String message, Throwable t) {
	super(message, t);
    }

    public CastleException(int responseCode, Error error, URI uri) {
        super(error.getMessage());
        this.responseCode = responseCode;
        this.error = error;
        this.uri = uri;
    }

    public CastleException(int responseCode, Error error, URI uri, Throwable t) {
        super(error.getMessage(), t);
        this.responseCode = responseCode;
        this.error = error;
        this.uri = uri;
    }
    
    public CastleException() {
	
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Error getError() {
        return error;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public void printStackTrace() {
        System.err.println("ResponseCode : " + this.responseCode);
        if(this.error != null) {
            System.err.println("Error type ["+this.error.getType()+"], message ["+this.error.getMessage()+"]");
        }
        super.printStackTrace();
    }
}
