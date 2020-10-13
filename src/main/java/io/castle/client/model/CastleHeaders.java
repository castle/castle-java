package io.castle.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CastleHeaders {

    private List<CastleHeader> headers;

    public List<CastleHeader> getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder(new CastleHeaders());
    }

    public void setHeaders(List<CastleHeader> headers) {
        this.headers = headers;
    }

    public CastleHeaders(List<CastleHeader> headers) { this.headers = headers; }

    public CastleHeaders() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastleHeaders that = (CastleHeaders) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return "CastleHeaders{" +
                "headers=" + headers +
                '}';
    }

    public static class Builder {
        private CastleHeaders headers;
        private ArrayList<CastleHeader> headerList;

        public Builder(CastleHeaders headers) {
            this.headers = headers;
            this.headerList = new ArrayList<>();
        }

        public CastleHeaders build() {
            headers.setHeaders(headerList);
            return headers;
        }

        public Builder add(String key, String value) {
            this.headerList.add(new CastleHeader(key, value));
            return this;
        }
    }
}
