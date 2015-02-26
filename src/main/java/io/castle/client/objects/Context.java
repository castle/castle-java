package io.castle.client.objects;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Context {
    private String ip;
    @JsonProperty("user_agent")
    private UserAgent userAgent;
    private Location location;

    public String getIp() {
	return ip;
    }

    public void setIp(String ip) {
	this.ip = ip;
    }

    public UserAgent getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(UserAgent userAgent) {
	this.userAgent = userAgent;
    }

    public Location getLocation() {
	return location;
    }

    public void setLocation(Location location) {
	this.location = location;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Context)) return false;

	Context context = (Context) o;

	if (ip != null ? !ip.equals(context.ip) : context.ip != null) return false;
	if (location != null ? !location.equals(context.location) : context.location != null) return false;
	if (userAgent != null ? !userAgent.equals(context.userAgent) : context.userAgent != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = ip != null ? ip.hashCode() : 0;
	result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
	result = 31 * result + (location != null ? location.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "Context{" +
		"ip='" + ip + '\'' +
		", userAgent=" + userAgent +
		", location=" + location +
		'}';
    }
}
