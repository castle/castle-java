package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pairing extends BaseRequesterObject {


    @JsonProperty("default")
    private boolean markedAsDefault;
    private String handle;
    private Map<String,String> config;
    private String type;
    private boolean verified;

    public boolean isMarkedAsDefault() {
	return markedAsDefault;
    }

    public void setMarkedAsDefault(boolean markedAsDefault) {
	this.markedAsDefault = markedAsDefault;
    }

    public String getHandle() {
	return handle;
    }

    public void setHandle(String handle) {
	this.handle = handle;
    }

    public Map<String, String> getConfig() {
	return config;
    }

    public void setConfig(Map<String, String> config) {
	this.config = config;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public boolean isVerified() {
	return verified;
    }

    public void setVerified(boolean verified) {
	this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Pairing)) return false;
	if (!super.equals(o)) return false;

	Pairing pairing = (Pairing) o;

	if (markedAsDefault != pairing.markedAsDefault) return false;
	if (verified != pairing.verified) return false;
	if (config != null ? !config.equals(pairing.config) : pairing.config != null) return false;
	if (handle != null ? !handle.equals(pairing.handle) : pairing.handle != null) return false;
	if (type != null ? !type.equals(pairing.type) : pairing.type != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	result = 31 * result + (markedAsDefault ? 1 : 0);
	result = 31 * result + (handle != null ? handle.hashCode() : 0);
	result = 31 * result + (config != null ? config.hashCode() : 0);
	result = 31 * result + (type != null ? type.hashCode() : 0);
	result = 31 * result + (verified ? 1 : 0);
	return result;
    }

    @Override
    public String toString() {
	return "Pairing{" +
		"markedAsDefault=" + markedAsDefault +
		", handle='" + handle + '\'' +
		", config=" + config +
		", type='" + type + '\'' +
		", verified=" + verified +
		"} " + super.toString();
    }

    public static class Requester{
	private UserInfoHeader info;
	private Session session;

	private Requester() {}

	private Requester(UserInfoHeader info) {
	    this.info = info;
	}

	private Requester(Session session) {
	    this.session = session;
	}

	public static Requester get() {
	    return new Requester();
	}

	public static Requester get(UserInfoHeader info) {
	    return new Requester(info);
	}

	public static Requester get(Session session) {
	    return new Requester(session);
	}

	public HttpClient getClient(URI uri, UserInfoHeader info, Session session) {
	    return new HttpClient(uri, info, session);
	}

	public Requester setSession(Session session) {
	    this.session = session;
	    return this;
	}

	public Requester setUserInfoHeader(UserInfoHeader info) {
	    this.info = info;
	    return this;
	}

	public Pairing create(User user, PairingRequest request) {
	    String path = "users/" + user.getId() + "/pairings";
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).query(parsePairingRequest(request)).build(), this.info, this.session);
	    return client.post(new TypeReference<Pairing>() {}, null);
	}

	private Map<String, String> parsePairingRequest(PairingRequest request) {
	    Map<String, String> query = new HashMap<>();
	    query.put("type", request.getType());
	    if(request.getType().equals("phone_number")) {
		query.put("number", request.getHandle());
	    } else if(request.getType().equals("yubikey")) {
		query.put("otp", request.getHandle());
	    }
	    if(request.isDefault()) {
		query.put("set_default", "true");
	    }
	    return query;
	}

	public List<Pairing> listAllParings(User user) {
	    String path = "users/" + user.getId() + "/pairings";
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).build(), this.info, this.session);
	    return client.get(new TypeReference<List<Pairing>>() {
	    });
	}

	public Pairing verify(User user, Pairing pairing, String response) {
	    String path = "users/" + user.getId() + "/pairings/" + pairing.getId() + "/verify";
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).query("response", response).build(), this.info, this.session);
	    return client.post(new TypeReference<Pairing>() {}, null);
	}

	public Pairing find(User user, String pairingId) {
	    String path = "users/" + user.getId() + "/pairings/" + pairingId;
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).build(), this.info, this.session);
	    return client.get(new TypeReference<Pairing>() {
	    });
	}

	public void delete(User user, Pairing pairing) {
	    String path = "users/" + user.getId() + "/pairings/" + pairing.getId();
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).build(), this.info, this.session);
	    client.delete(new TypeReference<Void>() {});
	}

	public void setAsDefault(User user, Pairing pairing) {
	    String path = "users/" + user.getId() + "/pairings/" + pairing.getId() + "/set_default";
	    HttpClient client = getClient(UriBuilder.newBuilder().path(path, false).build(), this.info, this.session);
	    client.post(new TypeReference<Void>() {}, null);
	}
    }

    public static Requester setUserInfoHeader(UserInfoHeader info) {
	return Requester.get(info);
    }

    public static Requester setSession(Session session) {
	return Requester.get(session);
    }

    public static Pairing create(User user, PairingRequest request) {
	return Requester.get().create(user, request);
    }

    public static List<Pairing> listAllParings(User user) {
	return Requester.get().listAllParings(user);
    }

    public static Pairing verify(User user, Pairing pairing, String response) {
	return Requester.get().verify(user, pairing, response);
    }

    public static Pairing find(User user, String pairingId) {
	return Requester.get().find(user,pairingId);
    }

    public static void delete(User user, Pairing pairing) {
	Requester.get().delete(user, pairing);
    }

    public static void setAsDefault(User user, Pairing pairing) {
	Requester.get().setAsDefault(user, pairing);
    }
}
