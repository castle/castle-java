package io.castle.client.model;

public enum AuthenticateAction {
    ALLOW, DENY, CHALLENGE;

    public static AuthenticateAction fromAction(String action) {
        if( action == null) {
            return null;
        }
        if( action.compareToIgnoreCase(ALLOW.name())==0) {
            return ALLOW;
        }
        if( action.compareToIgnoreCase(DENY.name())==0) {
            return DENY;
        }
        if( action.compareToIgnoreCase(CHALLENGE.name())==0) {
            return CHALLENGE;
        }
        return null;
    }
}
