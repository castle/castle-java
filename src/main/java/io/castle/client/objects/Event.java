package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Event extends BaseRequesterObject {
    private Context context;
    private Map<String, Object> details;
    private String name;
    private String userId;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        if (!super.equals(o)) return false;

        Event event = (Event) o;

        if (context != null ? !context.equals(event.context) : event.context != null) return false;
        if (details != null ? !details.equals(event.details) : event.details != null) return false;
        if (name != event.name) return false;
        if (userId != userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "context=" + context +
                ", details=" + details +
                ", name=" + name +
                "} " + super.toString();
    }

    public static class Requester {
        private UserInfoHeader info;
        private Session session;

        private Requester() {

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

        public Requester setUserInfoHeader(UserInfoHeader info) {
            this.info = info;
            return this;
        }

        public Event authenticate(Event event) {
            String path = "authenticate";
            URI eventUri = UriBuilder.newBuilder().path(path, false).build();
            HttpClient client = new HttpClient(eventUri, this.info, this.session);
            Event resultEvent = client.post(new TypeReference<Event>() {
            }, event);
            return resultEvent;
        }

        public Event track(Event event) {
            String path = "track";
            URI eventUri = UriBuilder.newBuilder().path(path, false).build();
            HttpClient client = new HttpClient(eventUri, this.info, this.session);
            Event resultEvent = client.post(new TypeReference<Event>() {
            }, event);
            return resultEvent;
        }
    }

    public static Requester setUserInfoHeader(UserInfoHeader info) {
        return Requester.get(info);
    }
}
