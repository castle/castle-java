package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.model.CastleContext;
import io.castle.client.internal.model.CastleHeader;
import io.castle.client.internal.model.CastleHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CastleContextBuilder {

    private CastleContext context;
    private CastleHeaders headers;

    public CastleContextBuilder() {
        context = new CastleContext();
    }

    public CastleContext build() {
        context.setHeaders(headers);
        return context;
    }

    public CastleContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        context.setClientId(setClientIdFromHttpServletRequest(request));
        this.headers = setCastleHeadersFromHttpServletRequest(request);
        context.setUserAgent(setUserAgentFromHttpServletRequest(request));
        context.setIp(setIpFromHttpServletRequest(request));
        return this;
    }

    public CastleContextBuilder withConfiguration(CastleConfiguration configuration) {

        //Todo: implement proper behaviour for using whitelist and blacklist from the configuration
        List<CastleHeader> headersList = new ArrayList<>();
        CastleHeaders newHeaders = new CastleHeaders();
        newHeaders.setHeaders(headersList);

        this.headers = newHeaders;
        return this;
    }

    private String setIpFromHttpServletRequest(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private String setUserAgentFromHttpServletRequest(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    private CastleHeaders setCastleHeadersFromHttpServletRequest(HttpServletRequest request) {
        ArrayList<CastleHeader> castleHeadersList = new ArrayList<>();
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String key = headerNames.nextElement();
            // TODO: what headers should we exclude from the context object? Or maybe none? Is this why we have Whitelist and blacklist?
            if (!key.equals("User-Agent") && !key.equals("X-Castle-Client-Id")) {
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
