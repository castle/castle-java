package io.castle.client.model;

public class ReviewContext {
    private String ip;
    private ReviewUserAgent userAgent;
    private ReviewLocation location;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ReviewUserAgent getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(ReviewUserAgent userAgent) {
        this.userAgent = userAgent;
    }

    public ReviewLocation getLocation() {
        return location;
    }

    public void setLocation(ReviewLocation location) {
        this.location = location;
    }
}
