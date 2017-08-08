package io.castle.client.deprecated.objects;

import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.deprecated.http.HttpClient;
import io.castle.client.deprecated.http.UriBuilder;

public class Monitoring {

    public static class Requester {
	private UserInfoHeader info;

	private Requester() {}

	private Requester(UserInfoHeader info) {
	    this.info = info;
	}

	public static Requester get() {
	    return new Requester();
	}

	public static Requester get(UserInfoHeader info) {
	    return new Requester(info);
	}

	public void heartBeat(Session session) {
	    HttpClient client = new HttpClient(UriBuilder.newBuilder().path("heartbeat").build(), this.info, session);
	    client.post(new TypeReference<Void>() {}, null);
	}
    }

    public static Requester setUserInfoHeader(UserInfoHeader info) {
	return Requester.get(info);
    }

    public static void heartBeat(Session session) {
	Requester.get().heartBeat(session);
    }
}
