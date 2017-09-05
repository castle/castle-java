package io.castle.client.model;

import io.castle.client.api.CastleApi;

/**
 * Stores settings for either using an AuthenticationAction or throwing a TimeoutException,
 * if a Castle API authenticate call fails.
 * <p>
 * If a {@link CastleApi#authenticate} call fails during an authentication attempt, the failover strategy specifies
 * which {@link AuthenticateAction} should be used.
 * Alternatively, a TimeoutException instance could be configured.
 */
public class AuthenticateFailoverStrategy {

    private final AuthenticateAction defaultAction;
    private final boolean throwTimeoutException;

    private AuthenticateFailoverStrategy(AuthenticateAction defaultAction, boolean throwTimeoutException) {
        this.defaultAction = defaultAction;
        this.throwTimeoutException = throwTimeoutException;
    }

    /**
     * Sets the default authenticationAction for failed requests.
     *
     * @param action the authenticateAction that will be used as a default for failed requests
     */
    public AuthenticateFailoverStrategy(AuthenticateAction action) {
        this(action, false);
    }

    /**
     * Creates an authenticationStrategy whose policy is to throw a timeoutException.
     */
    public AuthenticateFailoverStrategy() {
        this(null, true);
    }


    /**
     * Gets the authenticateAction that this strategy uses for failover.
     *
     * @return the authenticateAction configured as default; null if there is no default AuthenticateAction
     */
    public AuthenticateAction getDefaultAction() {
        return defaultAction;
    }

    /**
     * Checks whether the failover strategy is configured to throw a timeoutException.
     *
     * @return true if the failover strategy is set to throw a timeoutException, false otherwise
     */
    public boolean isThrowTimeoutException() {
        return throwTimeoutException;
    }
}
