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
        for (RiskPolicyType kind : RiskPolicyType.class.getEnumConstants()) {
            if (kind.name().equalsIgnoreCase(type)) {
                return kind;
            }
        }
        return null;
    }
}
