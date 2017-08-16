package io.castle.client.internal.model;

import java.util.List;

public class CastleHeaders {

    private List<CastleHeader> headers;

    public List<CastleHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<CastleHeader> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "CastleHeaders{" +
                "headers=" + headers +
                '}';
    }
}
