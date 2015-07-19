package io.castle.client;

import io.castle.client.exceptions.CastleException;
import io.castle.client.objects.Pairing;
import io.castle.client.objects.PairingRequest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class PairingINTTest extends GenericINTTest {

    @Test
    public void createGoogleAuthenticatorPairing() {
	PairingRequest request = PairingRequest.authenticatorParing(true);
	Pairing pairing = Pairing.setUserInfoHeader(userHeader).create(testUser, request);
	assertNotNull(pairing);
	assertNotNull(pairing.getHandle());
	assertTrue(pairing.getConfig().containsKey("qr_url"));
	System.out.println(pairing.getConfig().get("qr_url"));
	String response = "CHANGE_ME";
	Pairing verified = Pairing.setUserInfoHeader(userHeader).verify(testUser, pairing, response);
	assertTrue(verified.isVerified());
    }

    @Test
    public void findPairing() {
	PairingRequest request = PairingRequest.authenticatorParing(true);
	Pairing pairing = Pairing.setUserInfoHeader(userHeader).create(testUser, request);

	Pairing found = Pairing.setUserInfoHeader(userHeader).find(testUser, pairing.getId());
	assertNotNull(found);
	assertEquals(pairing, found);
    }

    @Test
    public void listPairings() {
	PairingRequest request = PairingRequest.authenticatorParing(true);
	Pairing pairing = Pairing.setUserInfoHeader(userHeader).create(testUser, request);

	System.out.println(pairing.getConfig().get("qr_url"));
	String response = "CHANGE_ME";
	Pairing verified = Pairing.setUserInfoHeader(userHeader).verify(testUser, pairing, response);

	List<Pairing> pairings = Pairing.setUserInfoHeader(userHeader).listAllParings(testUser);
	assertNotNull(pairings);
	assertEquals(verified, pairings.get(0));
    }

    @Test
    public void deletePairing() {
	PairingRequest request = PairingRequest.authenticatorParing(true);
	Pairing pairing = Pairing.setUserInfoHeader(userHeader).create(testUser, request);
	Pairing found = Pairing.setUserInfoHeader(userHeader).find(testUser, pairing.getId());
	assertNotNull(found);
	assertEquals(pairing, found);
	Pairing.setUserInfoHeader(userHeader).delete(testUser,pairing);
	try {
	    Pairing.setUserInfoHeader(userHeader).find(testUser, pairing.getId());
	    fail();
	} catch(CastleException e) {
	    assertEquals(404, e.getResponseCode());
	}
    }

    @Test
    public void setAsDefaultNotVerified() {
	PairingRequest request = PairingRequest.authenticatorParing(false);
	Pairing pairing = Pairing.setUserInfoHeader(userHeader).create(testUser, request);
	Pairing found = Pairing.setUserInfoHeader(userHeader).find(testUser, pairing.getId());
	assertEquals(pairing,found);
	try {
	    Pairing.setUserInfoHeader(userHeader).setAsDefault(testUser, pairing);
	} catch(CastleException e) {
	    assertEquals(400, e.getResponseCode());
	}
    }
}
