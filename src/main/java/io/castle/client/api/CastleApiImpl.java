package io.castle.client.api;

import io.castle.client.model.AuthenticateAction;

//TODO create provider facade to make internal api not public
//TODO implementation is a fake class to define the example app
public class CastleApiImpl implements CastleApi {
    @Override
    public CastleApi mergeContext(Object additionalContext) {
        return this;
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId) {
        return AuthenticateAction.ALLOW;
    }

    @Override
    public AuthenticateAction authenticate(String event, String userId, Object properties) {
        return AuthenticateAction.ALLOW;
    }

    @Override
    public void track(String event) {

    }

    @Override
    public void track(String event, String userId) {

    }

    @Override
    public void track(String event, String userId, Object properties) {

    }

    @Override
    public void identify(String userId, boolean active, Object traits) {

    }
}
