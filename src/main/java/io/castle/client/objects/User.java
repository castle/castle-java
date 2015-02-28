package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;


@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class User extends BaseRequesterObject {

    private String email;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("last_seen_at")
    private Date lastSeenAt;
    private String name;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private URI image;
    @JsonProperty("mfa_enabled")
    private boolean mfaEnabled;
    @JsonProperty("sessions_count")
    private Integer sessionsCount;
    @JsonProperty("locked_at")
    private Date lockedAt;
    private String username;
    @JsonProperty("devices_count")
    private Integer devicesCount;
    @JsonProperty("alerts_count")
    private Integer alertsCount;
    @JsonProperty("network_count")
    private Integer networkCount;
    private Integer score;
    
    public User() {
	
    }
    
    public User(String userId, String email) {
	setId(userId);
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    public Date getLastSeenAt() {
	return lastSeenAt;
    }

    public void setLastSeenAt(Date lastSeenAt) {
	this.lastSeenAt = lastSeenAt;
    }

    public String getName() {
	return name;
    }

    public void setName(String userName) {
	this.name = userName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public URI getImage() {
	return image;
    }

    public void setImage(URI image) {
	this.image = image;
    }

    public boolean isMfaEnabled() {
	return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnables) {
	this.mfaEnabled = mfaEnables;
    }

    public Integer getSessionsCount() {
	return sessionsCount;
    }

    public void setSessionsCount(Integer sessionsCount) {
	this.sessionsCount = sessionsCount;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDevicesCount() {
	return devicesCount;
    }

    public void setDevicesCount(Integer devicesCount) {
	this.devicesCount = devicesCount;
    }

    public Integer getAlertsCount() {
	return alertsCount;
    }

    public void setAlertsCount(Integer alertsCount) {
	this.alertsCount = alertsCount;
    }

    public Integer getNetworkCount() {
	return networkCount;
    }

    public void setNetworkCount(Integer networkCount) {
	this.networkCount = networkCount;
    }

    public Integer getScore() {
	return score;
    }

    public void setScore(Integer score) {
	this.score = score;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof User)) return false;
	if (!super.equals(o)) return false;

	User user = (User) o;

	if (mfaEnabled != user.mfaEnabled) return false;
	if (devicesCount != null ? !devicesCount.equals(user.devicesCount) : user.devicesCount != null) return false;
	if (email != null ? !email.equals(user.email) : user.email != null) return false;
	if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
	if (image != null ? !image.equals(user.image) : user.image != null) return false;
	if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
	if (lastSeenAt != null ? !lastSeenAt.equals(user.lastSeenAt) : user.lastSeenAt != null) return false;
	if (lockedAt != null ? !lockedAt.equals(user.lockedAt) : user.lockedAt != null) return false;
	if (name != null ? !name.equals(user.name) : user.name != null) return false;
	if (sessionsCount != null ? !sessionsCount.equals(user.sessionsCount) : user.sessionsCount != null)
	    return false;
	if (updatedAt != null ? !updatedAt.equals(user.updatedAt) : user.updatedAt != null) return false;
	if (username != null ? !username.equals(user.username) : user.username != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	result = 31 * result + (email != null ? email.hashCode() : 0);
	result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
	result = 31 * result + (lastSeenAt != null ? lastSeenAt.hashCode() : 0);
	result = 31 * result + (name != null ? name.hashCode() : 0);
	result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
	result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
	result = 31 * result + (image != null ? image.hashCode() : 0);
	result = 31 * result + (mfaEnabled ? 1 : 0);
	result = 31 * result + (sessionsCount != null ? sessionsCount.hashCode() : 0);
	result = 31 * result + (lockedAt != null ? lockedAt.hashCode() : 0);
	result = 31 * result + (username != null ? username.hashCode() : 0);
	result = 31 * result + (devicesCount != null ? devicesCount.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "User{" +
		"email='" + email + '\'' +
		", updatedAt=" + updatedAt +
		", lastSeenAt=" + lastSeenAt +
		", name='" + name + '\'' +
		", firstName='" + firstName + '\'' +
		", lastName='" + lastName + '\'' +
		", image=" + image +
		", mfaEnabled=" + mfaEnabled +
		", sessionsCount=" + sessionsCount +
		", lockedAt=" + lockedAt +
		", username='" + username + '\'' +
		", devicesCount=" + devicesCount +
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

        public Requester setSession(Session session) {
            this.session = session;
            return this;
        }

        public Requester setUserInfoHeader(UserInfoHeader info) {
            this.info = info;
            return this;
        }

        public void create(User user) {
            HttpClient client = new HttpClient(UriBuilder.newBuilder().path("users").build(),this.info, this.session);
            client.post(new TypeReference<Void>() {
            }, user);
        }

        public UserCollection listUsers() {
            String path = "users";
            HttpClient client = new HttpClient(UriBuilder.newBuilder().path(path).query(BaseCollection.buildPageQuery(1,UserCollection.getPageSize())).build(),this.info, this.session);
            List<User> page = client.get(new TypeReference<List<User>>() {});
            return new UserCollection(page,path, this.info, this.session);
        }

        public User find(String userId) {
            URI findUri = UriBuilder.newBuilder().path("users/" + userId, false).build();
            HttpClient client = new HttpClient(findUri, this.info, this.session);
            return client.get(new TypeReference<User>() {});
        }

        public User update(User user) {
            URI updateUri = UriBuilder.newBuilder().path("users/" + user.getId(), false).build();
            HttpClient client = new HttpClient(updateUri, this.info, this.session);
            return client.put(new TypeReference<User>() {}, user);
        }

        public void delete(User user) {
            URI deleteUri = UriBuilder.newBuilder().path("users/" + user.getId(), false).build();
            HttpClient client = new HttpClient(deleteUri, this.info, this.session);
            client.delete(new TypeReference<User>() {});
        }

        public User enableMFA(User user) {
            URI lockUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/enable_mfa", false).build();
            HttpClient client = new HttpClient(lockUri, this.info, this.session);
            return client.post(new TypeReference<User>() {}, null);
        }

        public User disableMFA(User user) {
            URI lockUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/disable_mfa", false).build();
            HttpClient client = new HttpClient(lockUri, this.info, this.session);
            return client.post(new TypeReference<User>() {}, null);
        }

        public User lock(User user) {
            URI lockUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/lock", false).build();
            HttpClient client = new HttpClient(lockUri, this.info, this.session);
            return client.post(new TypeReference<User>() {}, null);
        }

        public User unlock(User user) {
            URI unlockUri = UriBuilder.newBuilder().path("users/" + user.getId() + "/unlock", false).build();
            HttpClient client = new HttpClient(unlockUri, this.info, this.session);
            return client.post(new TypeReference<User>() {
            }, null);
        }
    }

    public static Requester setUserInfoHeaders(UserInfoHeader info) {
        return Requester.get(info);
    }

    public static Requester setSession(Session session) {
        return Requester.get(session);
    }

    public static void create(User user) {
        Requester.get().create(user);
    }

    public static UserCollection listUsers() {
        return Requester.get().listUsers();
    }

    public static User find(String userId) {
        return Requester.get().find(userId);
    }

    public static User update(User user) {
        return Requester.get().update(user);
    }

    public static void delete(User user) {
        Requester.get().delete(user);
    }

    public static User enableMFA(User user) {
        return Requester.get().enableMFA(user);
    }

    public static User disableMFA(User user) {
        return Requester.get().disableMFA(user);
    }

    public static User lock(User user) {
        return Requester.get().lock(user);
    }

    public static User unlock(User user) {
        return Requester.get().unlock(user);
    }
}
