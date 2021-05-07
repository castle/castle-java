package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

public class CastlePayloadBuilder {

    private CastlePayload payload;
    private CastleHeaders headers;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;
    private final HeaderNormalizer headerNormalizer = new HeaderNormalizer();

    public CastlePayloadBuilder(CastleConfiguration configuration, CastleGsonModel model) {
        this.configuration = configuration;
        this.model = model;
        payload = new CastlePayload();
    }

    public CastlePayload build() {
        payload.setHeaders(headers);
        return payload;
    }

    public CastlePayloadBuilder fingerprint(String fingerprint) {
        payload.setFingerprint(fingerprint);
        return this;
    }

    public CastlePayloadBuilder fingerprint(boolean fingerprint) {
        payload.setFingerprint(fingerprint);
        return this;
    }

    public CastlePayloadBuilder ip(String ip) {
        payload.setIp(ip);
        return this;
    }

    public CastlePayloadBuilder headers(CastleHeaders headers) {
        this.headers = headers;
        return this;
    }

    public CastlePayloadBuilder fromHttpServletRequest(HttpServletRequest request) {
        payload.setFingerprint(setFingerprintFromHttpServletRequest(request));
        this.headers = setCastleHeadersFromHttpServletRequest(request);

        payload.setIp(request.getRemoteAddr());
        if (configuration.getIpHeaders() != null) {
            for (String header : configuration.getIpHeaders()) {
                if (request.getHeader(header) != null) {
                    payload.setIp(request.getHeader(header));
                    break;
                }
            }
        }
        return this;
    }

    public CastlePayloadBuilder fromJson(String optionsString) {
        this.payload = model.getGson().fromJson(optionsString, CastlePayload.class);
        this.headers = payload.getHeaders();
        return this;
    }

    public String toJson() {
        return model.getGson().toJson(build());
    }

    /**
     * Load the headers from the HttpRequest.
     * A header will be passed only when it is not on the denyList and it appears on the allowList
     *
     * @param request The HttpRequest containing the headers.
     * @return headers Model for castle backend.
     */
    private CastleHeaders setCastleHeadersFromHttpServletRequest(HttpServletRequest request) {
        ArrayList<CastleHeader> castleHeadersList = new ArrayList<>();
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String key = headerNames.nextElement();
            String headerValue = request.getHeader(key);
            addHeaderValue(castleHeadersList, key, headerValue);
        }
        //A CGI specific header is added for compliance with other castle sdk libraries
        addHeaderValue(castleHeadersList, "REMOTE_ADDR",request.getRemoteAddr());

        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(castleHeadersList);
        return headers;
    }

    private void addHeaderValue(ArrayList<CastleHeader> castleHeadersList, String key, String headerValue) {
        String keyNormalized = headerNormalizer.normalize(key);
        if (configuration.getDenyListHeaders().contains(keyNormalized)) {
            // Scrub header since it is denyListed
            castleHeadersList.add(new CastleHeader(key, "true"));
            return;
        }

        // No allowList set, everything is allowListed
        if (configuration.getAllowListHeaders().isEmpty()) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        } else if (configuration.getAllowListHeaders().contains(keyNormalized)) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        } else {
            // Add scrubbed header
            castleHeadersList.add(new CastleHeader(key, "true"));
        }
    }

    /**
     * Extract the fingerprint from the request.
     * If header 'X-Castle-Client-Id' is set use that value, if not use __cid cookie, if none is set default to false
     * @param request HttpServletRequest to extract fingerprint from
     * @return a string fingerprint or false
     */
    private Object setFingerprintFromHttpServletRequest(HttpServletRequest request) {
        String cid = request.getHeader("X-Castle-Client-Id");

        // If client id header is not included, check cookie
        if (cid == null || cid.isEmpty()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("__cid")) {
                        cid = cookie.getValue();
                    }
                }
            }
        }

        // If cid could not be extracted, default to false
        if (cid == null) {
            return false;
        }

        return cid;
    }
}
