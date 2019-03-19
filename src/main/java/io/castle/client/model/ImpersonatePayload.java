package io.castle.client.model;

import io.castle.client.internal.config.CastleConfiguration;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.internal.utils.CastleContextBuilder;

public class ImpersonatePayload {
    private final String userId;
    private final CastleContext context;

    public ImpersonatePayload(String userId, CastleConfiguration configuration, CastleGsonModel model) {
        this.userId = userId;
        this.context = new CastleContextBuilder(configuration, model).build();
    }
}
