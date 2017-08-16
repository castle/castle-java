package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.model.CastleContext;
import io.castle.client.internal.model.CastleHeader;
import io.castle.client.internal.model.CastleHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

public class CastleContextBuilder {

    private CastleContext context;
    private CastleHeaders headers;
    private final CastleConfiguration configuration;

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
     * @param request
     * @return headers model for castle backend
     */
    private CastleHeaders setCastleHeadersFromHttpServletRequest(HttpServletRequest request) {
        ArrayList<CastleHeader> castleHeadersList = new ArrayList<>();
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String key = headerNames.nextElement();
            String keyLower = key.toLowerCase();
            if (!configuration.getBlackListHeaders().contains(keyLower)
                    && configuration.getWhiteListHeaders().contains(keyLower)) {
                castleHeadersList.add(new CastleHeader(key, request.getHeader(key)));
            }
        }
        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(castleHeadersList);
        return headers;
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
