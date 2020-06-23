package io.castle.client.model;

/**
 *  Type of Risk policy
 */
public enum RiskPolicyType {
    BOT, AUTHENTICATION;

    /**
     * Returns an RiskPolicyType from a string representing its name.
     *
     * @param type string representing the name of the RiskPolicyType, case-insensitive
     * @return the enum value matching the name, or null if it does not match any enum
     */
    public static RiskPolicyType fromType(String type) {
        if (type == null) {
            return null;
        }
        try {
            return RiskPolicyType.valueOf(type);
        } catch (IllegalArgumentException e) {
            // no op, use string compare functions.
        }
        if (type.compareToIgnoreCase(BOT.name()) == 0) {
            return BOT;
        }
        if (type.compareToIgnoreCase(AUTHENTICATION.name()) == 0) {
            return AUTHENTICATION;
        }
        return null;
    }
}
