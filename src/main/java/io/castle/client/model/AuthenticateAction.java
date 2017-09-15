package io.castle.client.model;

/**
 * Action that needs to be taken after a login attempt.
 * <p>
 * See the documentation for the semantics of each case.
 * It can be null.
 *
 * @see <a href="https://castle.io/docs/authentication">Adaptive authentication</a>
 */
public enum AuthenticateAction {
    ALLOW, DENY, CHALLENGE;

    /**
     * Returns an AuthenticateAction from a string representing its name.
     *
     * @param action string representing the name of the AuthenticateAction, case-insensitive
     * @return the enum value matching the name, or null if it does not match any enum
     */
    public static AuthenticateAction fromAction(String action) {
        if (action == null) {
            return null;
        }
        try {
            return AuthenticateAction.valueOf(action);
        } catch (IllegalArgumentException e) {
            // no op, use string compare functions.
        }
        if (action.compareToIgnoreCase(ALLOW.name()) == 0) {
            return ALLOW;
        }
        if (action.compareToIgnoreCase(DENY.name()) == 0) {
            return DENY;
        }
        if (action.compareToIgnoreCase(CHALLENGE.name()) == 0) {
            return CHALLENGE;
        }
        return null;
    }
}
