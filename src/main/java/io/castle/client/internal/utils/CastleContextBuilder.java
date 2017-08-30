package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleHeader;
import io.castle.client.model.CastleHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

public class CastleContextBuilder {

    private CastleContext context;
    private CastleHeaders headers;
    private final CastleConfiguration configuration;
    private final HeaderNormalizer headerNormalizer = new HeaderNormalizer();

    public CastleContextBuilder(CastleConfiguration configuration) {
        this.configuration = configuration;
        context = new CastleContext();
    }

    public CastleContext build() {
        context.setHeaders(headers);
        return context;
    }

    public CastleContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        context.setClientId(setClientIdFromHttpServletRequest(request));
        this.headers = setCastleHeadersFromHttpServletRequest(request);
        context.setUserAgent(request.getHeader("User-Agent"));
        context.setIp(request.getRemoteAddr());
        return this;
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
        if (!configuration.getBlackListHeaders().contains(keyNormalized)
                && configuration.getWhiteListHeaders().contains(keyNormalized)) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        }
    }

    private String setClientIdFromHttpServletRequest(HttpServletRequest request) {
        String cid = request.getHeader("X-Castle-Client-Id");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("__cid")) {
                    cid = cookie.getValue();
                }
            }
        }
        return cid;
    }

}
