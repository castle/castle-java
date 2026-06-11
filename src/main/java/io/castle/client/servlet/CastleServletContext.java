package io.castle.client.servlet;

import io.castle.client.Castle;
import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.CastleContextBuilder;
import io.castle.client.internal.utils.HeaderNormalizer;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleHeader;
import io.castle.client.model.CastleHeaders;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Servlet integration helper.
 * <p>
 * Reads the request context (IP, headers, user agent, client id) and the
 * webhook signature header from a {@code jakarta.servlet.http.HttpServletRequest}
 * and feeds them into the framework-agnostic SDK surface
 * ({@link CastleContextBuilder} and {@link Castle#verifyWebhookSignature(String, byte[])}).
 * <p>
 * Every reference to the Servlet API is contained in this class. Consumers on a
 * different stack (plain {@code byte[]} bodies, JAX-RS, {@code javax.servlet},
 * non-servlet frameworks) can build the context and verify webhooks without it.
 */
public final class CastleServletContext {

    private static final HeaderNormalizer HEADER_NORMALIZER = new HeaderNormalizer();

    private final CastleConfiguration configuration;
    private final CastleGsonModel model;

    public CastleServletContext(CastleConfiguration configuration, CastleGsonModel model) {
        this.configuration = configuration;
        this.model = model;
    }

    /**
     * Build a populated context builder from a servlet request.
     *
     * @param request the incoming servlet request
     * @return a {@link CastleContextBuilder} with IP, headers, user agent and client id applied
     */
    public CastleContextBuilder builder(HttpServletRequest request) {
        return populate(new CastleContextBuilder(configuration, model), configuration, request);
    }

    /**
     * Build a {@link CastleContext} from a servlet request.
     *
     * @param request the incoming servlet request
     * @return the populated context
     */
    public CastleContext toContext(HttpServletRequest request) {
        return builder(request).build();
    }

    /**
     * Apply the values extracted from a servlet request onto an existing builder.
     *
     * @param builder       the builder to populate
     * @param configuration the active SDK configuration (drives IP headers and allow/deny lists)
     * @param request       the incoming servlet request
     * @return the same builder, for chaining
     */
    public static CastleContextBuilder populate(CastleContextBuilder builder, CastleConfiguration configuration, HttpServletRequest request) {
        Object clientId = extractClientId(request);
        if (clientId instanceof String) {
            builder.clientId((String) clientId);
        } else {
            builder.clientId(false);
        }

        builder.headers(extractHeaders(configuration, request));

        Object userAgent = extractUserAgent(request);
        if (userAgent instanceof String) {
            builder.userAgent((String) userAgent);
        } else {
            builder.userAgent(false);
        }

        builder.ip(extractIp(configuration, request));
        return builder;
    }

    /**
     * Read the {@code X-Castle-Signature} header from a servlet request and verify it
     * against the supplied raw request body.
     *
     * @param castle  an initialized SDK instance
     * @param request the incoming webhook request
     * @param body    the raw request body bytes
     * @return {@code true} when the signature matches the computed signature
     */
    public static boolean verifyWebhookSignature(Castle castle, HttpServletRequest request, byte[] body) {
        if (request == null) {
            return false;
        }
        return castle.verifyWebhookSignature(request.getHeader(Castle.WEBHOOK_SIGNATURE_HEADER), body);
    }

    public static String extractIp(CastleConfiguration configuration, HttpServletRequest request) {
        if (configuration.getIpHeaders() != null) {
            for (String header : configuration.getIpHeaders()) {
                if (request.getHeader(header) != null) {
                    return request.getHeader(header);
                }
            }
        }
        return request.getRemoteAddr();
    }

    public static CastleHeaders extractHeaders(CastleConfiguration configuration, HttpServletRequest request) {
        ArrayList<CastleHeader> castleHeadersList = new ArrayList<>();
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String key = headerNames.nextElement();
            addHeaderValue(configuration, castleHeadersList, key, request.getHeader(key));
        }
        // A CGI specific header is added for compliance with other Castle SDK libraries
        addHeaderValue(configuration, castleHeadersList, "REMOTE_ADDR", request.getRemoteAddr());

        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(castleHeadersList);
        return headers;
    }

    private static void addHeaderValue(CastleConfiguration configuration, ArrayList<CastleHeader> castleHeadersList, String key, String headerValue) {
        String keyNormalized = HEADER_NORMALIZER.normalize(key);
        if (configuration.getDenyListHeaders().contains(keyNormalized)) {
            castleHeadersList.add(new CastleHeader(key, "true"));
            return;
        }

        if (configuration.getAllowListHeaders().isEmpty()) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        } else if (configuration.getAllowListHeaders().contains(keyNormalized)) {
            castleHeadersList.add(new CastleHeader(key, headerValue));
        } else {
            castleHeadersList.add(new CastleHeader(key, "true"));
        }
    }

    public static Object extractClientId(HttpServletRequest request) {
        String cid = request.getHeader("X-Castle-Client-Id");

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

        if (cid == null) {
            return false;
        }

        return cid;
    }

    public static Object extractUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
            return false;
        }

        return userAgent;
    }
}
