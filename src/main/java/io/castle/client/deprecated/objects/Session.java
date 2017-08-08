package io.castle.client.deprecated.objects;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.deprecated.http.HttpClient;
import io.castle.client.deprecated.http.UriBuilder;
import io.castle.client.deprecated.utils.CastleJWT;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Session extends BaseRequesterObject {

    private Context context;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private String token;

    public Context getContext() {
	return context;
    }

    public void setContext(Context context) {
	this.context = context;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public boolean hasExpired() {
	return new CastleJWT(token).hasExpired();
    }

    public boolean isValid() {
	return new CastleJWT(token).isValid();
    }

    public boolean requiresMFA() {
	return new CastleJWT(token).requiresMFA();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        if (!super.equals(o)) return false;

        Session session = (Session) o;

        if (context != null ? !context.equals(session.context) : session.context != null) return false;
        if (token != null ? !token.equals(session.token) : session.token != null) return false;
        if (updatedAt != null ? !updatedAt.equals(session.updatedAt) : session.updatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "context=" + context +
                ", updatedAt=" + updatedAt +
                ", token='" + token + '\'' +
                "} " + super.toString();
    }

    public static class Requester {

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

        public Requester setSession(Session session) {
            this.session = session;
            return this;
        }

        public Requester setUserInfoHeader(UserInfoHeader info) {
            this.info = info;
            return this;
        }

        public Session create(User user) {
            URI createUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/sessions", false).build();
            HttpClient client = new HttpClient(createUri, this.info, this.session);
            return client.post(new TypeReference<Session>() {}, Collections.singletonMap("user", user));
        }

        public SessionCollection listSessions(User user) {
            String path = "users/" + user.getId() + "/sessions";
            URI sessionsUri = UriBuilder.newBuilder().path(path, false).query(BaseCollection.buildPageQuery(1,SessionCollection.getPageSize())).build();
            HttpClient client = new HttpClient(sessionsUri, this.info, this.session);
            List<Session> page = client.get(new TypeReference<List<Session>>() {});
            return new SessionCollection(page,path, this.info, this.session);
        }

        public void deleteAllSessions(User user) {
            URI deleteUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/sessions", false).build();
            HttpClient client = new HttpClient(deleteUri, this.info, this.session);
            client.delete(new TypeReference<User>() {});
        }

        public Session find(String sessionId, User user) {
            URI sessionsUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/sessions/" + sessionId, false).build();
            HttpClient client = new HttpClient(sessionsUri, this.info, this.session);
            return client.get(new TypeReference<Session>() {});
        }

        public void delete(User user, Session session) {
            URI sessionsUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/sessions/" + session.getId(), false).build();
            HttpClient client = new HttpClient(sessionsUri, this.info, this.session);
            client.delete(new TypeReference<Session>() {
            });
        }


    }

    public static Requester setUserInfoHeaders(UserInfoHeader info) {
        return Requester.get(info);
    }

    public static Requester setSession(Session session) {
        return Requester.get(session);
    }

    public static Session create(User user) {
        return Requester.get().create(user);
    }

    public static SessionCollection listSessions(User user) {
        return Requester.get().listSessions(user);
    }

    public static void deleteAllSessions(User user) {
        Requester.get().deleteAllSessions(user);
    }

    public static Session find(String sessionId, User user) {
        return Requester.get().find(sessionId, user);
    }

    public static void delete(User user, Session session) {
        Requester.get().delete(user, session);
    }
}
