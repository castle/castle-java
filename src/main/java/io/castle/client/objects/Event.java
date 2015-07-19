package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.castle.client.deserializers.EventNameDeserializer;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;
import io.castle.client.serializers.EventNameSerializer;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Event extends BaseRequesterObject {

    public enum EventName {
        BACKUP_CODES_GENERATED("backup_codes.generated"),
        CHALLENGE_FAILES("challenge.failed"),
        CHALLENGE_VERIFIED("challenge.verified"),
        PARING_DESTROYED("pairing.destroyed"),
        PARING_SET_DEFAULT("pairing.set_default"),
        PARING_VERIFIED("pairing.verified"),
        SESSION_CREATED("session.created"),
        SESSION_DESTROYED("session.destroyed"),
        SESSION_FAILED("session.failed"),
        TRUSTED_DEVICE_CREATED("trusted_device.created"),
        TRUSTED_DEVICE_DESTROYED("trusted_device.destroyed"),
        USER_CREATED("user.created"),
        USER_UPDATED("user.updated"),
        USER_DESTROYED("user.destroyed"),
        USER_LOCKED("user.locked"),
        USER_UNLOCKED("user.unlocked"),
        USER_MFA_ENABLED("user.mfa_enabled"),
        USER_MFA_DISABLED("user.mfs_disabled"),
        LOGIN_SUCCEEDED("$login.succeeded");

        private final String eventName;

        EventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventName() {
            return this.eventName;
        }

        @Override
        public String toString() {
            return this.eventName;
        }

        public static EventName fromName(String name) {
            for (EventName v : values()) {
                if (v.getEventName().equals(name)) {
                    return v;
                }
            }
            throw new IllegalArgumentException("No event by name: " + name);
        }

    }

    private Context context;
    private Map<String, Object> details;
    @JsonDeserialize(using = EventNameDeserializer.class)
    @JsonSerialize(using = EventNameSerializer.class)
    private EventName name;

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

    public EventName getName() {
        return name;
    }

    public void setName(EventName name) {
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

        public Requester setSession(Session session) {
            this.session = session;
            return this;
        }

        public EventCollection listAllUserEvents(User user) {
            String path = "users/" + user.getId() + "/events";
            URI eventUri = UriBuilder.newBuilder().path(path, false).query(BaseCollection.buildPageQuery(1, EventCollection.getPageSize())).build();
            HttpClient client = new HttpClient(eventUri, this.info, this.session);
            List<Event> page = client.get(new TypeReference<List<Event>>() {
            });
            return new EventCollection(page, path, this.info, this.session);
        }

        public EventCollection listAllEvents() {
            return listAllEvents(null);
        }

        public EventCollection listAllEvents(Map<String, String> query) {
            String path = "events";
            Map<String, String> queryParams = BaseCollection.buildPageQuery(1, EventCollection.getPageSize());
            if (query != null) {
                queryParams.putAll(query);
            }
            URI eventUri = UriBuilder.newBuilder().path(path, false).query(queryParams).build();
            HttpClient client = new HttpClient(eventUri, this.info, this.session);
            List<Event> page = client.get(new TypeReference<List<Event>>() {
            });
            return new EventCollection(page, path, query, this.info, this.session);
        }

        public Event trackEvent(Event event) {
            String path = "events";
            URI eventUri = UriBuilder.newBuilder().path(path, false).build();
            HttpClient client = new HttpClient(eventUri, this.info, this.session);
            Event resultEvent = client.post(new TypeReference<Event>() {
            }, event);
            return resultEvent;
        }


    }

    public static Requester setSession(Session session) {
        return Requester.get(session);
    }

    public static Requester setUserInfoHeader(UserInfoHeader info) {
        return Requester.get(info);
    }

    public static EventCollection listAllUserEvents(User user) {
        return Requester.get().listAllUserEvents(user);
    }

    public static EventCollection listAllEvents() {
        return Requester.get().listAllUserEvents(null);
    }

    public static EventCollection listAllEvents(Map<String, String> query) {
        return Requester.get().listAllEvents(query);
    }


}
