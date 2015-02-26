package io.castle.client.utils;

import io.castle.client.Castle;
import io.castle.client.exceptions.CastleException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CastleJWT {

    private String sessionToken;
    private String header;
    private String token;
    private String signature;

    public CastleJWT(String token) {
	String[] tokenData = token.split("\\.");
	this.sessionToken = token;
	this.header = tokenData[0];
	this.token = tokenData[1];
	this.signature = tokenData[2];
    }

    public boolean isValid() {
	String calculatedHash;
	try {
	    calculatedHash = Utils.signature(Castle.getSecret(), this.getHeader(), this.header + "." + this.token);
	    return calculatedHash.equals(this.signature);
	} catch (Exception e) {
	    throw new CastleException("Failed to generate hash", e);
	}
    }

    public boolean requiresMFA() {
	return this.getHeader().containsKey("vfy") && (Integer)this.getHeader().get("vfy") != 0;
    }

    public Map<String, Object> getHeader() {
	ObjectMapper mapper = new ObjectMapper();
	TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
	};
	try {
	    return mapper.readValue(Utils.base64Decode(this.header), typeReference);
	} catch (IOException e) {
	    throw new CastleException("Failed to parse header", e);
	}
    }

    public Map<String, Object> getPayload() {
	ObjectMapper mapper = new ObjectMapper();
	TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
	};
	try {
	    return mapper.readValue(Utils.base64Decode(this.token), typeReference);
	} catch (IOException e) {
	    throw new CastleException("Failed to serialize from JSON", e);
	}
    }

    public String getSessionToken() {
	if (this.isValid()) {
	    return this.sessionToken;
	}
	return null;
    }

    public boolean hasExpired() {
	Long exp = Long.parseLong(this.getHeader().get("exp").toString()) * 1000L;
	Calendar expDate = Calendar.getInstance();
	expDate.setTime(new Date(exp));
	Calendar now = Calendar.getInstance();
	return now.after(expDate);
    }
}
