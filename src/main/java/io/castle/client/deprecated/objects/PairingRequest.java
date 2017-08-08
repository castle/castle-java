package io.castle.client.deprecated.objects;

public class PairingRequest {

    private String type;
    private String handle;
    private boolean isDefault;

    private PairingRequest(String type, String handle, boolean isDefault) {
	this.type = type;
	this.handle = handle;
	this.isDefault = isDefault;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getHandle() {
	return handle;
    }

    public void setHandle(String handle) {
	this.handle = handle;
    }

    public boolean isDefault() {
	return isDefault;
    }

    public void setDefault(boolean isDefault) {
	this.isDefault = isDefault;
    }

    public static PairingRequest authenticatorParing(boolean isDefault) {
	return new PairingRequest("authenticator",null,isDefault);
    }

    public static PairingRequest phoneNumberPairing(String phoneNumber, boolean isDefault) {
	return new PairingRequest("phone_number", phoneNumber, isDefault);
    }

    public static PairingRequest yubikeyPairing(String otp, boolean isDefault) {
	return new PairingRequest("yubikey", otp, isDefault);
    }

    public static PairingRequest u2fPairing(boolean isDefault) {
	return new PairingRequest("u2f", null, isDefault);
    }
}