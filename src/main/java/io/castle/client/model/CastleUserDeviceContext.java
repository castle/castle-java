package io.castle.client.model;

public class CastleUserDeviceContext {
    private String ip;
    private DeviceUserAgent userAgent;
    private String type;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public DeviceUserAgent getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(DeviceUserAgent userAgent) {
        this.userAgent = userAgent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
