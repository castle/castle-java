package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.util.Map;

public class Challenge extends BaseRequesterObject{

    @JsonProperty("delivery_method")
    private String deliveryMethod;
    private Pairing pairing;
    private Map<String, String> params;

    public String getDeliveryMethod() {
	return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
	this.deliveryMethod = deliveryMethod;
    }

    public Pairing getPairing() {
	return pairing;
    }

    public void setPairing(Pairing pairing) {
	this.pairing = pairing;
    }

    public Map<String, String> getParams() {
	return params;
    }

    public void setParams(Map<String, String> params) {
	this.params = params;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Challenge)) return false;
	if (!super.equals(o)) return false;

	Challenge challenge = (Challenge) o;

	if (deliveryMethod != null ? !deliveryMethod.equals(challenge.deliveryMethod) : challenge.deliveryMethod != null)
	    return false;
	if (pairing != null ? !pairing.equals(challenge.pairing) : challenge.pairing != null) return false;
	if (params != null ? !params.equals(challenge.params) : challenge.params != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	result = 31 * result + (deliveryMethod != null ? deliveryMethod.hashCode() : 0);
	result = 31 * result + (pairing != null ? pairing.hashCode() : 0);
	result = 31 * result + (params != null ? params.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "Challenge{" +
		"deliveryMethod='" + deliveryMethod + '\'' +
		", pairing=" + pairing +
		", params=" + params +
		"} " + super.toString();
    }

    public static class Requester {
	private UserInfoHeader info;
	private Session session;

	private Requester(){

	}

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

	public Requester setSession(Session session) {
	    this.session = session;
	    return this;
	}

	public Requester setUserInfoHeader(UserInfoHeader info) {
	    this.info = info;
	    return this;
	}

	public Challenge create(User user) {
	    return create(user, null);
	}

	public Challenge create(User user, String pairingId) {
	    String path = "users/" + user.getId() + "/challenges";
	    UriBuilder uri = UriBuilder.newBuilder().path(path, false);
	    if(pairingId != null) {
		uri.query("pairing_id", pairingId);
	    }
	    HttpClient client = new HttpClient(uri.build(), this.info, this.session);
	    return client.post(new TypeReference<Challenge>() {}, null);
	}

	public Challenge find(User user, String challengeId) {
	    String path = "users/" + user.getId() + "/challenges/" + challengeId;
	    HttpClient client = new HttpClient(UriBuilder.newBuilder().path(path, false).build(), this.info, this.session);
	    return client.get(new TypeReference<Challenge>() {});
	}

	public void verify(User user, Challenge challenge, String response) {
	    String path = "users/" + user.getId() + "/challenges/" + challenge.getId() + "/verify";
	    HttpClient client = new HttpClient(UriBuilder.newBuilder().path(path, false).query("response", response).build(), this.info, this.session);
	    client.post(new TypeReference<Challenge>() {}, null);
	}

    }

    public Requester setSession(Session session) {
	return Requester.get(session);
    }

    public Requester setUserInfoHeader(UserInfoHeader info) {
	return Requester.get(info);
    }

    public static Challenge create(User user) {
	return Requester.get().create(user);
    }

    public static Challenge create(User user, String pairingId) {
	return Requester.get().create(user,pairingId);
    }

    public static Challenge find(User user, String challengeId) {
	return Requester.get().find(user, challengeId);
    }

    public static void verify(User user, Challenge challenge, String response) {
	Requester.get().verify(user, challenge, response);
    }
}
