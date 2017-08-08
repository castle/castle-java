package io.castle.client.model;

public class AuthenticateStrategy {

    private final AuthenticateAction defaultAction;
    private final boolean throwTimeoutException;

    private AuthenticateStrategy(AuthenticateAction defaultAction, boolean throwTimeoutException) {
        this.defaultAction = defaultAction;
        this.throwTimeoutException = throwTimeoutException;
    }

    public AuthenticateStrategy(AuthenticateAction action) {
        this(action, false);
    }

    public AuthenticateStrategy() {
        this(null, true);
    }

    public AuthenticateAction getDefaultAction() {
        return defaultAction;
    }

    public boolean isThrowTimeoutException() {
        return throwTimeoutException;
    }
}
