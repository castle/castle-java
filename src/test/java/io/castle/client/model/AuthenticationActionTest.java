package io.castle.client.model;

import org.junit.Assert;
import org.junit.Test;

public class AuthenticationActionTest {

    @Test
    public void testFromAllow() {
        testFromActionMethod("AllOw", AuthenticateAction.ALLOW);
    }

    @Test
    public void testFromDeny() {
        testFromActionMethod("Deny", AuthenticateAction.DENY);
    }

    @Test
    public void testFromChallenge() {
        testFromActionMethod("cHallenge", AuthenticateAction.CHALLENGE);
    }

    @Test
    public void testFromNull() {
        testFromActionMethod(null, null);
    }

    @Test
    public void testFromEmptyString() {
        testFromActionMethod("", null);
    }

    @Test
    public void testFromWrongString() {
        testFromActionMethod("getMeANull", null);
    }

    private void testFromActionMethod(String from, AuthenticateAction expected) {

        AuthenticateAction authenticateAction = AuthenticateAction.fromAction(from);
        Assert.assertEquals(expected, authenticateAction);

    }


}
