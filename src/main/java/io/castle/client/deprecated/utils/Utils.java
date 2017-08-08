package io.castle.client.deprecated.utils;

import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.jersey.core.util.Base64;
import io.castle.client.deprecated.exceptions.CastleException;

class Utils {

    private Utils() {
    }

    static String base64Decode(String str) {
	int rem = str.length() % 4;
	if(rem > 0) {
	    for(int i=0; i<4-rem; i++) {
		str += "=";
	    }
	}
	return new String(Base64.decode(str));
    }

    static String base64Encode(byte[] str) {
	String s = new String(Base64.encode(str));
	s = s.replace("\n", "").replace("\r", "");
	return s;
    }

    static String urlDecode(String str) {
	str = str.replace('_', '/').replace('-', '+');
	return str;
    }

    static String urlEncode(String str) {
	str = str.replace('+', '-').replace('/', '_');
	return str.replace("=", "");
    }

    static String[] splitToken(String token) {
	String[] parts = token.split("\\.");
	if (parts.length != 3) {
	    throw new CastleException("Invalid format for JWT");
	}
	return parts;
    }

    static String signature(final String appSecret, final Map<String, Object> header, final String data) {
	if (header.get("alg").equals("HS256")) {
	    try {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secretKey);
		return urlEncode(Utils.base64Encode(sha256_HMAC.doFinal(data.getBytes())));
	    } catch (Exception e) {
		throw new CastleException("Failed to create hash!");
	    }
	}
	throw new CastleException("At this time only HMACSHA256 is supported");
    }
}
