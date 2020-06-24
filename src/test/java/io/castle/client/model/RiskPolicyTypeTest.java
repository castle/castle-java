package io.castle.client.model;

import org.junit.Assert;
import org.junit.Test;

public class RiskPolicyTypeTest {

    @Test
    public void testFromAllow() {
        testFromTypeMethod("BOt", RiskPolicyType.BOT);
    }

    @Test
    public void testFromDeny() {
        testFromTypeMethod("Authentication", RiskPolicyType.AUTHENTICATION);
    }

    @Test
    public void testFromNull() {
        testFromTypeMethod(null, null);
    }

    @Test
    public void testFromEmptyString() {
        testFromTypeMethod("", null);
    }

    @Test
    public void testFromWrongString() {
        testFromTypeMethod("getMeANull", null);
    }

    private void testFromTypeMethod(String from, RiskPolicyType expected) {

        RiskPolicyType type = RiskPolicyType.fromType(from);
        Assert.assertEquals(expected, type);

    }


}
