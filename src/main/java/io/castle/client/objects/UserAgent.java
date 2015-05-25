package io.castle.client.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserAgent {
    private String raw;
    private String browser;
    private String version;
    private String platform;
    private String os;
    @JsonIgnore
    private boolean mobile;

    public String getRaw() {
	return raw;
    }

    public void setRaw(String raw) {
	this.raw = raw;
    }

    public String getBrowser() {
	return browser;
    }

    public void setBrowser(String browser) {
	this.browser = browser;
    }

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public String getPlatform() {
	return platform;
    }

    public void setPlatform(String platform) {
	this.platform = platform;
    }

    public String getOs() {
	return os;
    }

    public void setOs(String os) {
	this.os = os;
    }

    public boolean isMobile() {
	return mobile;
    }

    public void setMobile(boolean mobile) {
	this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof UserAgent)) return false;

	UserAgent userAgent = (UserAgent) o;

	if (mobile != userAgent.mobile) return false;
	if (browser != null ? !browser.equals(userAgent.browser) : userAgent.browser != null) return false;
	if (os != null ? !os.equals(userAgent.os) : userAgent.os != null) return false;
	if (platform != null ? !platform.equals(userAgent.platform) : userAgent.platform != null) return false;
	if (raw != null ? !raw.equals(userAgent.raw) : userAgent.raw != null) return false;
	if (version != null ? !version.equals(userAgent.version) : userAgent.version != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = raw != null ? raw.hashCode() : 0;
	result = 31 * result + (browser != null ? browser.hashCode() : 0);
	result = 31 * result + (version != null ? version.hashCode() : 0);
	result = 31 * result + (platform != null ? platform.hashCode() : 0);
	result = 31 * result + (os != null ? os.hashCode() : 0);
	result = 31 * result + (mobile ? 1 : 0);
	return result;
    }

    @Override
    public String toString() {
	return "UserAgent{" +
		"raw='" + raw + '\'' +
		", browser='" + browser + '\'' +
		", version='" + version + '\'' +
		", platform='" + platform + '\'' +
		", os='" + os + '\'' +
		", mobile=" + mobile +
		'}';
    }
}
