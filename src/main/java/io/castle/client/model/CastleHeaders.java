package io.castle.client.model;

import java.util.ArrayList;
import java.util.List;

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
            return self();
        }

        private Builder self() {
            return this;
        }
    }
}
