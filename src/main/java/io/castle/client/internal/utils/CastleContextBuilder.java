package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleDevice;
import io.castle.client.model.CastleHeader;
import io.castle.client.model.CastleHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

public class CastleContextBuilder {

    private CastleContext context;
    private CastleHeaders headers;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;
    private final HeaderNormalizer headerNormalizer = new HeaderNormalizer();

    public CastleContextBuilder(CastleConfiguration configuration, CastleGsonModel model) {
        this.configuration = configuration;
        this.model = model;
        context = new CastleContext();
    }

    public CastleContext build() {
        context.setHeaders(headers);
        return context;
    }

    public CastleContextBuilder active(boolean active) {
        context.setActive(active);
        return this;
    }

    public CastleContextBuilder fingerprint(String fingerprint) {
        context.setFingerprint(fingerprint);
        return this;
    }

    public CastleContextBuilder fingerprint(boolean fingerprint) {
        context.setFingerprint(fingerprint);
        return this;
    }

    public CastleContextBuilder device(CastleDevice device) {
        context.setDevice(device);
        return this;
    }

    public CastleContextBuilder ip(String ip) {
        context.setIp(ip);
        return this;
    }

    public CastleContextBuilder headers(CastleHeaders headers) {
        this.headers = headers;
        return this;
    }

    public CastleContextBuilder userAgent(String userAgent) {
        context.setUserAgent(userAgent);
        return this;
    }

    public CastleContextBuilder userAgent(boolean userAgent) {
        context.setUserAgent(userAgent);
        return this;
    }

    public CastleContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        context.setFingerprint(setFingerprintFromHttpServletRequest(request));
        this.headers = setCastleHeadersFromHttpServletRequest(request);
        context.setUserAgent(setUserAgentFromHttpServletRequest(request));

        context.setIp(request.getRemoteAddr());
        if (configuration.getIpHeaders() != null) {
            for (String header : configuration.getIpHeaders()) {
                if (request.getHeader(header) != null) {
                    context.setIp(request.getHeader(header));
                    break;
                }
            }
        }
        return this;
    }

    public CastleContextBuilder fromJson(String contextString) {
        this.context = model.getGson().fromJson(contextString, CastleContext.class);
        this.headers = context.getHeaders();
        return this;
    }

    public String toJson() {
        return model.getGson().toJson(build());
    }

    /**
     * Load the headers from the HttpRequest.
     * A header will be passed only when it is not on the blacklist and it appears on the whitelist
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
        if (configuration.getBlackListHeaders().contains(keyNormalized)) {
            // Scrub header since it is blacklisted
            castleHeadersList.add(new CastleHeader(key, "true"));
            return;
        }

        // No whitelist set, everything is whitelisted
        if (configuration.getWhiteListHeaders().isEmpty()) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        } else if (configuration.getWhiteListHeaders().contains(keyNormalized)) {
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

    /**
     * Extract the User Agent from the request.
     * If header 'User-Agent' is set use that value, if not default to false
     * @param request HttpServletRequest to extract User Agent from
     * @return a string User Agent or false
     */
    private Object setUserAgentFromHttpServletRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        // If User Agent header is not present, default to false
        if (userAgent == null) {
            return false;
        }

        return userAgent;
    }

}
