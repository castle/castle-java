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
    private final CastleGsonModel model;
    private final CastleConfiguration configuration;

    public CastleContextBuilder(CastleConfiguration configuration, CastleGsonModel model) {
        this.configuration = configuration;
        this.model = model;
        context = new CastleContext();
    }

    public CastleContext build() {
        return context;
    }

    public CastleContextBuilder active(boolean active) {
        context.setActive(active);
        return this;
    }


    public CastleContextBuilder device(CastleDevice device) {
        context.setDevice(device);
        return this;
    }

    public CastleContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        return this;
    }

    public CastleContextBuilder fromJson(String contextString) {
        this.context = model.getGson().fromJson(contextString, CastleContext.class);
        return this;
    }

    public String toJson() {
        return model.getGson().toJson(build());
    }

}
