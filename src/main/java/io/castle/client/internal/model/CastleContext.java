package io.castle.client.internal.model;

import com.google.gson.annotations.SerializedName;

public class CastleContext {
    private boolean active = true;
    private CastleDevice device;
    private String clientId;
    private CastlePage page;
    private CastleReferrer referrer;
    private CastleHeaders headers;
    private String ip;
    private CastleSdkRef library = new CastleSdkRef();
    private String locale;
    private CastleLocation location;
    private CastleNetwork network;
    private CastleOS os;
    private CastleScreen screen;
    private String timezone;

    @SerializedName("userAgent")
    private String userAgent;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CastleDevice getDevice() {
        return device;
    }

    public void setDevice(CastleDevice device) {
        this.device = device;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public CastlePage getPage() {
        return page;
    }

    public void setPage(CastlePage page) {
        this.page = page;
    }

    public CastleReferrer getReferrer() {
        return referrer;
    }

    public void setReferrer(CastleReferrer referrer) {
        this.referrer = referrer;
    }

    public CastleHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(CastleHeaders headers) {
        this.headers = headers;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public CastleSdkRef getLibrary() {
        return library;
    }

    public void setLibrary(CastleSdkRef library) {
        this.library = library;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public CastleLocation getLocation() {
        return location;
    }

    public void setLocation(CastleLocation location) {
        this.location = location;
    }

    public CastleNetwork getNetwork() {
        return network;
    }

    public void setNetwork(CastleNetwork network) {
        this.network = network;
    }

    public CastleOS getOs() {
        return os;
    }

    public void setOs(CastleOS os) {
        this.os = os;
    }

    public CastleScreen getScreen() {
        return screen;
    }

    public void setScreen(CastleScreen screen) {
        this.screen = screen;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
