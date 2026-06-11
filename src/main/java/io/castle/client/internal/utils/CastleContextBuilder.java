package io.castle.client.internal.utils;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.CastleContext;
import io.castle.client.model.CastleDevice;
import io.castle.client.model.CastleHeaders;
import io.castle.client.servlet.CastleServletContext;

import jakarta.servlet.http.HttpServletRequest;

public class CastleContextBuilder {

    private CastleContext context;
    private CastleHeaders headers;
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

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

    public CastleContextBuilder clientId(String clientId) {
        context.setClientId(clientId);
        return this;
    }

    public CastleContextBuilder clientId(boolean clientId) {
        context.setClientId(clientId);
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

    /**
     * Populate the builder from a servlet request.
     * <p>
     * Delegates to {@link CastleServletContext}, which holds the only references to
     * the Servlet API. Consumers on other stacks can use {@link #ip(String)},
     * {@link #headers(CastleHeaders)} and {@link #userAgent(String)} directly.
     *
     * @param request the incoming servlet request
     * @return this builder, for chaining
     */
    public CastleContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        return CastleServletContext.populate(this, configuration, request);
    }

    public CastleContextBuilder fromJson(String contextString) {
        this.context = model.getGson().fromJson(contextString, CastleContext.class);
        this.headers = context.getHeaders();
        return this;
    }

    public String toJson() {
        return model.getGson().toJson(build());
    }

}
