package io.castle.client.objects;

public class UserInfoHeader {

    private String ip;
    private String userAgent;

    public String getIp() {
	return ip;
    }

    public void setIp(String ip) {
	this.ip = ip;
    }

    public String getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(String userAgent) {
	this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof UserInfoHeader)) return false;

	UserInfoHeader that = (UserInfoHeader) o;

	if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
	if (userAgent != null ? !userAgent.equals(that.userAgent) : that.userAgent != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = ip != null ? ip.hashCode() : 0;
	result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "UserInfoData{" +
		"ip='" + ip + '\'' +
		", userAgent='" + userAgent + '\'' +
		'}';
    }
}
